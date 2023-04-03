package wombatukun.tests.test11.authservice.events;

public interface UserEventPublisher {
    void sendEvent(UserEvent event);
}
