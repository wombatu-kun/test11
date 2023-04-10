package wombatukun.tests.test11.queryservice.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.queryservice.dao.entities.Order;
import wombatukun.tests.test11.queryservice.dao.entities.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order mapEventToEntity(OrderEvent event, Order order, User user, User courier) {
        if (order == null) {
            order = new Order();
            order.setId(event.getId());
        }
        order.setCost(event.getCost());
        order.setStatus(event.getStatus());
        switch (event.getStatus()) {
        case CREATED: order.setCreatedAt(event.getTimestamp()); break;
        case ASSIGNED: order.setAssignedAt(event.getTimestamp()); break;
        case SHIPPED: order.setShippedAt(event.getTimestamp()); break;
        case DELIVERED: order.setDeliveredAt(event.getTimestamp()); break;
        case CANCELLED: order.setCancelledAt(event.getTimestamp()); break;
        }
        order.setUser(user);
        order.setCourier(courier);
        return order;
    }

}
