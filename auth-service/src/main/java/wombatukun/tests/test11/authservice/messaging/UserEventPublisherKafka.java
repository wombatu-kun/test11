package wombatukun.tests.test11.authservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.common.messaging.UserEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventPublisherKafka implements UserEventPublisher {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Override
    public void sendEvent(UserEvent event) {
        kafkaTemplate.sendDefault(event);
    }

}
