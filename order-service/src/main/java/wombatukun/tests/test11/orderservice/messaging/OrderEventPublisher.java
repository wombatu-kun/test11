package wombatukun.tests.test11.orderservice.messaging;

import wombatukun.tests.test11.common.messaging.OrderEvent;

public interface OrderEventPublisher {

    void sendEvent(OrderEvent event);

}
