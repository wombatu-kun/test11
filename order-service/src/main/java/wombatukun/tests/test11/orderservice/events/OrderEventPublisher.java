package wombatukun.tests.test11.orderservice.events;

public interface OrderEventPublisher {
    void sendEvent(OrderEvent event);
}
