package wombatukun.tests.test11.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import wombatukun.tests.test11.common.dto.PageDto;
import wombatukun.tests.test11.common.exceptions.OperationNotPermittedException;
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException;
import wombatukun.tests.test11.common.security.AuthInfo;
import wombatukun.tests.test11.common.security.Role;
import wombatukun.tests.test11.orderservice.dao.entities.Details;
import wombatukun.tests.test11.orderservice.dao.entities.Order;
import wombatukun.tests.test11.orderservice.dao.projections.OrderCount;
import wombatukun.tests.test11.orderservice.dao.repositories.DetailsRepository;
import wombatukun.tests.test11.orderservice.dao.repositories.OrderRepository;
import wombatukun.tests.test11.orderservice.dao.specifications.OrderSpec;
import wombatukun.tests.test11.orderservice.dto.AssignOrderForm;
import wombatukun.tests.test11.orderservice.dto.DetailsDto;
import wombatukun.tests.test11.orderservice.dto.OrderDto;
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm;
import wombatukun.tests.test11.orderservice.dto.UserOrderForm;
import wombatukun.tests.test11.orderservice.enums.Status;
import wombatukun.tests.test11.orderservice.mappers.OrderMapper;
import wombatukun.tests.test11.orderservice.messaging.OrderEventPublisher;

import java.util.List;
import java.util.Set;

import static wombatukun.tests.test11.orderservice.enums.Status.ASSIGNED;
import static wombatukun.tests.test11.orderservice.enums.Status.CREATED;
import static wombatukun.tests.test11.orderservice.enums.Status.DELIVERED;
import static wombatukun.tests.test11.orderservice.enums.Status.SHIPPED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DetailsRepository detailsRepository;
    private final OrderMapper orderMapper;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional(readOnly = true)
    @Override
    public List<OrderCount> countTotalsByStates() {
        return orderRepository.countTotalsByStatuses();
    }

    @Override
    @Transactional(readOnly = true)
    public DetailsDto getOrderDetailsById(Authentication authentication, Long orderId) {
        Order order = findOrderById(orderId);
        AuthInfo auth = AuthInfo.fromAuthentication(authentication);
        if (Role.ROLE_ADMIN.equals(auth.getRole())
                || (Role.ROLE_USER == auth.getRole() && auth.getId().equals(order.getUserId()))
                || (Role.ROLE_COURIER == auth.getRole() && auth.getId().equals(order.getCourierId()))) {
            return orderMapper.mapEntityToDetailsDto(order);
        } else {
            throw new OperationNotPermittedException("access denied");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<OrderDto> search(Authentication authentication, SearchOrderForm form) {
        AuthInfo auth = AuthInfo.fromAuthentication(authentication);
        if (auth.getRole() == Role.ROLE_USER) {
            form.setUserId(auth.getId());
        } else if (auth.getRole() == Role.ROLE_COURIER) {
            form.setCourierId(auth.getId());
        }
        OrderSpec spec = orderMapper.mapSearchFormToSpec(form);
        Pageable pageable = PageRequest.of(form.getPage(), form.getPageSize());
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        return PageDto.of(orders, orderMapper::mapEntityToOrderDto);
    }

    @Transactional
    @Override
    public DetailsDto createOrder(Authentication authentication, UserOrderForm form) {
        AuthInfo auth = AuthInfo.fromAuthentication(authentication);
        form.setUserId(auth.getId());
        Order order = orderMapper.mapFormToEntity(form);
        order = orderRepository.save(order);
        orderEventPublisher.sendEvent(orderMapper.mapEntityToEvent(order));
        return orderMapper.mapEntityToDetailsDto(order);
    }

    @Override
    @Transactional
    public DetailsDto assignOrder(Authentication authentication, Long orderId, AssignOrderForm form) {
        Order order = findOrderById(orderId);
        if (Set.of(CREATED, ASSIGNED).contains(order.getStatus())) {
            order.setCourierId(form.getCourierId());
            order.setStatus(ASSIGNED);
            if (form.getCost() != null) {
                Details details = order.getDetails();
                details.setCost(form.getCost());
            }
            order = orderRepository.save(order);
            orderEventPublisher.sendEvent(orderMapper.mapEntityToEvent(order));
            return orderMapper.mapEntityToDetailsDto(order);
        }  else {
            throw new OperationNotPermittedException("unable to reassign this order");
        }
    }

    @Override
    @Transactional
    public DetailsDto updateDestination(Authentication authentication, Long orderId, String destination) {
        Assert.hasText(destination, "new Destination must not be empty");
        Order order = findOrderById(orderId);
        AuthInfo auth = AuthInfo.fromAuthentication(authentication);
        if (auth.getId().equals(order.getUserId()) && Set.of(CREATED, ASSIGNED).contains(order.getStatus())) {
            order.getDetails().setDestination(destination);
            detailsRepository.save(order.getDetails());
            return orderMapper.mapEntityToDetailsDto(order);
        } else {
            throw new OperationNotPermittedException("unable to change destination for this order");
        }
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Authentication authentication, Long orderId, Status status) {
        Assert.notNull(status, "new Status must not be null");
        Order order = findOrderById(orderId);
        AuthInfo auth = AuthInfo.fromAuthentication(authentication);
        switch (auth.getRole()) {
            case ROLE_ADMIN: order = updateStatusByAdmin(order, status); break;
            case ROLE_COURIER: order = updateStatusByCourier(auth.getId(), order, status); break;
            default: throw new OperationNotPermittedException("unable to update status");
        }
        orderEventPublisher.sendEvent(orderMapper.mapEntityToEvent(order));
        return orderMapper.mapEntityToOrderDto(order);
    }

    private Order updateStatusByAdmin(Order order, Status status) {
        if (status == ASSIGNED && order.getCourierId() == null) {
            throw new OperationNotPermittedException("unable to update status to ASSIGN, use assignOrder method");
        } else {
            if (status == CREATED) {
                order.setCourierId(null);
            }
            order.setStatus(status);
            return orderRepository.save(order);
        }
    }

    private Order updateStatusByCourier(Long courierId, Order order, Status status) {
        Status current = order.getStatus();
        if (courierId.equals(order.getCourierId()) &&
                ((current == ASSIGNED && status == SHIPPED) || (current == SHIPPED && status == DELIVERED))) {
            order.setStatus(status);
            return orderRepository.save(order);
        } else {
            throw new OperationNotPermittedException("unable to update status");
        }
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Authentication authentication, Long orderId) {
        Order order = findOrderById(orderId);
        AuthInfo auth = AuthInfo.fromAuthentication(authentication);
        if (auth.getId().equals(order.getUserId()) && Set.of(CREATED, ASSIGNED).contains(order.getStatus())) {
            order.setStatus(Status.CANCELLED);
            order = orderRepository.save(order);
            orderEventPublisher.sendEvent(orderMapper.mapEntityToEvent(order));
            return orderMapper.mapEntityToOrderDto(order);
        } else {
            throw new OperationNotPermittedException("unable to cancel this order");
        }
    }

    private Order findOrderById(Long orderId) {
        Assert.notNull(orderId, "orderId must not be null");
        return orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order not found"));
    }

}
