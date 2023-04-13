package wombatukun.tests.test11.queryservice.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.common.enums.OrderStatus
import wombatukun.tests.test11.common.messaging.OrderEvent
import wombatukun.tests.test11.queryservice.dao.entities.Order
import wombatukun.tests.test11.queryservice.dao.repositories.OrderRepository

@SpringBootTest
class OrderServiceTest extends Specification {

	@Subject
	@Autowired
	private OrderService orderService
	@Autowired
	private OrderRepository orderRepository

	def testHandleEvent() {
		given:
			OrderEvent event = OrderEvent.builder()
					.type(OrderEvent.class.name)
					.id(1L)
					.cost(BigDecimal.valueOf(4000L))
					.userId(2L)
					.courierId(4L)
					.timestamp(new Date())
					.status(OrderStatus.ASSIGNED)
					.correlationId("cor-id")
					.build()
		when:
			orderService.handleEvent(event)
			Order order = orderRepository.findById(event.id).orElse(null)
		then:
			verifyAll(order) {
				order != null
				order.assignedAt != null
				order.cost == event.cost
			}
	}

}
