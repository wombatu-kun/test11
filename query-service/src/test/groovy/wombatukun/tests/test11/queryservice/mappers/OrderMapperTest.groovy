package wombatukun.tests.test11.queryservice.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.common.enums.OrderStatus
import wombatukun.tests.test11.common.messaging.OrderEvent
import wombatukun.tests.test11.queryservice.dao.entities.Order
import wombatukun.tests.test11.queryservice.dao.entities.User

@SpringBootTest
class OrderMapperTest extends Specification {

	@Subject
	@Autowired
	private OrderMapper orderMapper

	def testMapEventToEntity() {
		given:
		User user = new User();
		user.id = 502
		User courier = new User()
		courier.id = 300
		OrderEvent event = OrderEvent.builder()
				.type(OrderEvent.class.name)
				.id(42L)
				.cost(BigDecimal.valueOf(4000L))
				.userId(user.id)
				.courierId(courier.id)
				.timestamp(new Date())
				.status(OrderStatus.ASSIGNED)
				.correlationId("cor-id")
				.build()
		when:
		Order order = orderMapper.mapEventToEntity(event, null, user, courier)
		println order
		then:
		verifyAll(order) {
			event.id == order.id
			event.timestamp == order.assignedAt
			event.cost == order.cost
			event.status == order.status
			user.id == order.user.id
			courier.id == order.courier.id
		}
	}

}
