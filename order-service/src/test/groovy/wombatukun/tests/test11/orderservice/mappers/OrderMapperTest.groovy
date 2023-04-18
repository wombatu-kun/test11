package wombatukun.tests.test11.orderservice.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.common.enums.OrderStatus
import wombatukun.tests.test11.common.messaging.OrderEvent
import wombatukun.tests.test11.common.usercontext.UserContext
import wombatukun.tests.test11.orderservice.dao.entities.Details
import wombatukun.tests.test11.orderservice.dao.entities.Order
import wombatukun.tests.test11.orderservice.dao.specifications.OrderSpec
import wombatukun.tests.test11.orderservice.dto.DetailsDto
import wombatukun.tests.test11.orderservice.dto.OrderDto
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm
import wombatukun.tests.test11.orderservice.dto.UserOrderForm
import wombatukun.tests.test11.orderservice.enums.Status

@SpringBootTest
class OrderMapperTest extends Specification {

	@Subject
	@Autowired
	private OrderMapper orderMapper

	private static Order createOrderEntity() {
		Order order = new Order()
		order.id = 42
		order.createdAt = new Date()
		order.status = Status.CREATED
		order.userId = 502L
		order.courierId = 305L
		return order
	}

	private static Details createDetailsEntity() {
		Details details = new Details()
		details.id = 42
		details.cost = new BigDecimal(4000L)
		details.departure = "Novosibirsk"
		details.destination = "Oslo"
		details.recipientName = "Vova"
		details.recipientPhone = "89039003333"
		return details
	}

	def testMapEntityToOrderDto() {
		given:
		Order order = createOrderEntity()
		when:
		OrderDto dto = orderMapper.mapEntityToOrderDto(order)
		println dto
		then:
		verifyAll(dto) {
			dto.id == order.id
			dto.createdAt == order.createdAt
			dto.status == order.status
			dto.userId == order.userId
			dto.courierId == order.courierId
		}
	}

	def testMapEntityToDetailsDto() {
		given:
		Order order = createOrderEntity()
		Details details = createDetailsEntity()
		order.setDetails(details)
		when:
		DetailsDto dto = orderMapper.mapEntityToDetailsDto(order)
		println dto
		then:
		verifyAll(dto) {
			dto.id == order.id
			dto.createdAt == order.createdAt
			dto.status == order.status
			dto.userId == order.userId
			dto.courierId == order.courierId
			dto.cost == details.cost
			dto.departure == details.departure
			dto.destination == details.destination
			dto.recipientName == details.recipientName
			dto.recipientPhone == details.recipientPhone
		}
	}

	def testMapFormToEntity() {
		given:
		UserOrderForm form = new UserOrderForm(502L, "Novosibirsk", "London", "Vova", "89039003333")
		when:
		Order order = orderMapper.mapFormToEntity(form)
		println order
		then:
		verifyAll(order) {
			order.id == null
			order.createdAt != null
			order.status == Status.CREATED
			order.details != null
			order.details.departure == form.departure
			order.details.destination == form.destination
			order.details.recipientName == form.recipientName
			order.details.recipientPhone == form.recipientPhone
			order.details.cost == null
			order.details.id == order.id
		}
	}

	def testMapSearchFormToSpec() {
		given:
		SearchOrderForm form = new SearchOrderForm(502L, 305L, new Date(), new Date(), Status.DELIVERED)
		when:
		OrderSpec spec = orderMapper.mapSearchFormToSpec(form)
		then:
		verifyAll(spec.query) {
			spec.query != null
			spec.query.userId == form.userId
			spec.query.courierId == form.courierId
			spec.query.status == form.status
			spec.query.createdFrom == form.createdFrom
			spec.query.createdTo == form.createdTo
		}
	}

	def testMapEntityToEvent() {
		given:
		Order order = createOrderEntity()
		Details details = createDetailsEntity()
		order.setDetails(details)
		UserContext.setCorrelationId("cor-id")
		when:
		OrderEvent event = orderMapper.mapEntityToEvent(order)
		println event
		then:
		verifyAll(event) {
			event.id == order.id
			event.timestamp != null
			event.status == OrderStatus.valueOf(order.status.name())
			event.userId == order.userId
			event.courierId == order.courierId
			event.cost == order.details.cost
			event.correlationId == "cor-id"
		}
	}

}
