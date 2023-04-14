package wombatukun.tests.test11.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.common.messaging.UserEvent;
import wombatukun.tests.test11.orderservice.dao.entities.UserCache;
import wombatukun.tests.test11.orderservice.services.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @KafkaListener(topics="${spring.kafka.consumer.user-topic:users}", groupId = "${spring.kafka.consumer.group-id:order-group}")
    public void handle(UserEvent event) {
        log.debug("Received user-event: {}", event);
        userService.saveUserCache(new UserCache(event.getId(), event.getRole(), event.getStatus()));
    }

}
