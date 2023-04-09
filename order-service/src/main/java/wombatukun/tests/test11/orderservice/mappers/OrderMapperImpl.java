package wombatukun.tests.test11.orderservice.mappers;

import org.springframework.stereotype.Component;
import wombatukun.tests.test11.common.enums.OrderStatus;
import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.common.usercontext.UserContext;
import wombatukun.tests.test11.orderservice.dao.entities.Details;
import wombatukun.tests.test11.orderservice.dao.entities.Order;
import wombatukun.tests.test11.orderservice.dao.queries.OrderQuery;
import wombatukun.tests.test11.orderservice.dao.specifications.OrderSpec;
import wombatukun.tests.test11.orderservice.dto.DetailsDto;
import wombatukun.tests.test11.orderservice.dto.OrderDto;
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm;
import wombatukun.tests.test11.orderservice.dto.UserOrderForm;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.util.Date;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto mapEntityToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .userId(order.getUserId())
                .courierId(order.getCourierId())
                .build();
    }

    @Override
    public DetailsDto mapEntityToDetailsDto(Order order) {
        Details details = order.getDetails();
        return DetailsDto.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .cost(details.getCost())
                .departure(details.getDeparture())
                .destination(details.getDestination())
                .recipientName(details.getRecipientName())
                .recipientPhone(details.getRecipientPhone())
                .userId(order.getUserId())
                .courierId(order.getCourierId())
                .build();
    }

    @Override
    public Order mapFormToEntity(UserOrderForm form) {
        Order order = new Order();
        order.setStatus(Status.CREATED);
        order.setCreatedAt(new Date());
        order.setUserId(form.getUserId());
        Details details = new Details();
        details.setDeparture(form.getDeparture());
        details.setDestination(form.getDestination());
        details.setRecipientName(form.getRecipientName());
        details.setRecipientPhone(form.getRecipientPhone());
        order.setDetails(details);
        return order;
    }

    @Override
    public OrderSpec mapSearchFormToSpec(SearchOrderForm form) {
        OrderQuery query = OrderQuery.builder()
                .userId(form.getUserId())
                .courierId(form.getCourierId())
                .status(form.getStatus())
                .createdFrom(form.getCreatedFrom())
                .createdTo(form.getCreatedTo())
                .build();
        return new OrderSpec(query);
    }

    @Override
    public OrderEvent mapEntityToEvent(Order order) {
        return OrderEvent.builder()
                .type(OrderEvent.class.getTypeName())
                .id(order.getId())
                .status(OrderStatus.valueOf(order.getStatus().name()))
                .timestamp(new Date())
                .userId(order.getUserId())
                .courierId(order.getCourierId())
                .cost(order.getDetails().getCost())
                .correlationId(UserContext.getCorrelationId())
                .build();
    }

}
