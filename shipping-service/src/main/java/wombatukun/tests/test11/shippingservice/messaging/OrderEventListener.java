package wombatukun.tests.test11.shippingservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache;
import wombatukun.tests.test11.shippingservice.services.CacheService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final CacheService cacheService;

    @KafkaListener(topics="${spring.kafka.template.default-topic:orders}", groupId = "${spring.kafka.consumer.group-id:shipping-group}")
    public void handle(OrderEvent event) {
        log.debug("Received order-event: {}", event);
        switch (event.getStatus()) {
        case SHIPPED:
        case ASSIGNED: cacheService.saveCache(new AssignmentCache(event.getId(), event.getCourierId())); break;
        case CREATED:
        case DELIVERED:
        case CANCELLED: cacheService.deleteCache(event.getId()); break;
        default: //do nothing
        }
    }


}
