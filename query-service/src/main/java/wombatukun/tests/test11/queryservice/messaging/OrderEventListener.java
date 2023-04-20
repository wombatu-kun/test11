package wombatukun.tests.test11.queryservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.queryservice.services.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    @KafkaListener(topics="${spring.kafka.consumer.order-topic:orders}", groupId = "${spring.kafka.consumer.group-id:query-group}")
    public void handle(OrderEvent event) {
        log.debug("Received order-event: {}", event);
        orderService.handleEvent(event);
    }

}
