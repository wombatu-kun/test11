package wombatukun.tests.test11.queryservice.mappers;

import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.queryservice.dao.entities.Order;
import wombatukun.tests.test11.queryservice.dao.entities.User;

public interface OrderMapper {

    Order mapEventToEntity(OrderEvent event, Order order, User user, User courier);

}
