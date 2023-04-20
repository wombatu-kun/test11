package wombatukun.tests.test11.authservice.messaging;

import wombatukun.tests.test11.common.messaging.UserEvent;

public interface UserEventPublisher {
    void sendEvent(UserEvent event);
}
