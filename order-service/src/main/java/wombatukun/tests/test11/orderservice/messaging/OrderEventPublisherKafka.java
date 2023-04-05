package wombatukun.tests.test11.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.common.messaging.OrderEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisherKafka implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Override
    public void sendEvent(OrderEvent event) {
        kafkaTemplate.sendDefault(event);
    }

}
