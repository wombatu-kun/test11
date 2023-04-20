package wombatukun.tests.test11.queryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.queryservice.dao.entities.Order;
import wombatukun.tests.test11.queryservice.dao.entities.User;
import wombatukun.tests.test11.queryservice.dao.repositories.OrderRepository;
import wombatukun.tests.test11.queryservice.mappers.OrderMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public void handleEvent(OrderEvent event) {
        User user = userService.findUserById(event.getUserId());
        User courier = event.getCourierId() == null ? null : userService.findUserById(event.getCourierId());
        Order order = orderMapper.mapEventToEntity(event, findOrderById(event.getId()), user, courier);
        orderRepository.save(order);
    }

    private Order findOrderById(Long orderId) {
        Assert.notNull(orderId, "orderId must not be null");
        return orderRepository.findById(orderId).orElse(null);
    }

}
