package wombatukun.tests.test11.queryservice.services;

import wombatukun.tests.test11.common.messaging.OrderEvent;

public interface OrderService {

    void handleEvent(OrderEvent event);

}
