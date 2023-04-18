package wombatukun.tests.test11.orderservice.services

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.common.dto.PageDto
import wombatukun.tests.test11.common.enums.UserStatus
import wombatukun.tests.test11.common.exceptions.OperationNotPermittedException
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException
import wombatukun.tests.test11.common.security.Role
import wombatukun.tests.test11.orderservice.dao.entities.Details
import wombatukun.tests.test11.orderservice.dao.entities.Order
import wombatukun.tests.test11.orderservice.dao.entities.UserCache
import wombatukun.tests.test11.orderservice.dao.projections.OrderCount
import wombatukun.tests.test11.orderservice.dao.repositories.DetailsRepository
import wombatukun.tests.test11.orderservice.dao.repositories.OrderRepository
import wombatukun.tests.test11.orderservice.dto.AssignOrderForm
import wombatukun.tests.test11.orderservice.dto.DetailsDto
import wombatukun.tests.test11.orderservice.dto.OrderDto
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm
import wombatukun.tests.test11.orderservice.dto.UserOrderForm
import wombatukun.tests.test11.orderservice.enums.Status
import wombatukun.tests.test11.orderservice.messaging.OrderEventPublisher

@SpringBootTest
class OrderServiceTest extends Specification {

	private static final Long ADMIN_ID = 1L
	private static final Long USER_ID = 2L
	private static final Long COURIER_ID = 3L

	@Autowired
	private OrderRepository orderRepository
	@Autowired
	private DetailsRepository detailsRepository
	@SpringBean
	private OrderEventPublisher orderEventPublisher = Mock()
	@SpringBean
	private UserService userService = Stub() {
		getUser(COURIER_ID) >> new UserCache(COURIER_ID, Role.ROLE_COURIER, UserStatus.ACTIVE)
		getUser(23L) >> new UserCache(23L, Role.ROLE_COURIER, UserStatus.SUSPENDED)
		getUser(24L) >> new UserCache(24L, Role.ROLE_USER, UserStatus.ACTIVE)
		getUser(25L) >> { throw new ResourceNotFoundException("no courier with id=25") }
		getUser(26L) >> { throw new RuntimeException("connection error") }
	}

	@Subject
	@Autowired
	private OrderService orderService

	def setup() {
		orderRepository.save(defaultOrder())
	}

	def cleanup() {
		detailsRepository.deleteAll()
		orderRepository.deleteAll()
	}

	def testCountTotalsByStatuses() {
		when:
		List<OrderCount> results = orderService.countTotalsByStatuses()
		println results
		then:
		verifyAll(results) {
			results.size() == 1
			results.get(0).status == Status.CREATED
			results.get(0).count == 1L
		}
	}

	def testSearch() {
		given:
		Order order = orderRepository.findAll().get(0)
		order.status = Status.ASSIGNED
		order.courierId = COURIER_ID
		orderRepository.save(order)
		Authentication author = makeAuthentication(Role.ROLE_USER, USER_ID)
		Authentication otherUser = makeAuthentication(Role.ROLE_USER, 100L)
		Authentication courier = makeAuthentication(Role.ROLE_COURIER, COURIER_ID)
		Authentication otherCourier = makeAuthentication(Role.ROLE_COURIER, 200L)
		Authentication admin = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		when:
		PageDto<OrderDto> userSearchOk = orderService.search(author, new SearchOrderForm(null, COURIER_ID, null, new Date(), Status.ASSIGNED))
		PageDto<OrderDto> userSearchEmpty = orderService.search(author, new SearchOrderForm(null, null, null, null, Status.CREATED))
		PageDto<OrderDto> otherUserSearch = orderService.search(otherUser, new SearchOrderForm(null, null, null, null, null))
		PageDto<OrderDto> courierSearchOk = orderService.search(courier, new SearchOrderForm(null, 300L, null, null, Status.ASSIGNED))
		PageDto<OrderDto> courierSearchEmpty = orderService.search(courier, new SearchOrderForm(null, null, new Date(), null, Status.ASSIGNED))
		PageDto<OrderDto> otherCourierSearch = orderService.search(otherCourier, new SearchOrderForm(null, null, null, null, null))
		PageDto<OrderDto> adminSearchOk = orderService.search(admin, new SearchOrderForm(USER_ID, COURIER_ID, null, null, null))
		PageDto<OrderDto> adminSearchEmpty = orderService.search(admin, new SearchOrderForm(USER_ID, 8L, null, null, null))
		then:
		verifyAll {
			userSearchOk.totalElements == 1
			userSearchOk.totalPages == 1
			userSearchOk.pageNumber == 0
			userSearchOk.page.get(0).userId == USER_ID
			userSearchOk.page.get(0).courierId == COURIER_ID
			userSearchOk.page.get(0).status == Status.ASSIGNED
			userSearchEmpty.totalElements == 0
			otherUserSearch.totalElements == 0
			courierSearchOk.totalElements == 1
			courierSearchEmpty.totalElements == 0
			otherCourierSearch.totalElements == 0
			adminSearchOk.totalElements == 1
			adminSearchEmpty.totalElements == 0
		}
	}

	def "testAssignOrder - non-existent order"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		when:
		orderService.assignOrder(auth, 1000L, new AssignOrderForm(COURIER_ID, BigDecimal.TEN))
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(ResourceNotFoundException)
		exception.message == "order not found"
	}

	def "testAssignOrder - incorrect status"() {
		given:
		Order order = orderRepository.findAll().get(0)
		order.status = Status.DELIVERED
		order.courierId = 99L
		orderRepository.save(order)
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		when:
		orderService.assignOrder(auth, order.id, new AssignOrderForm(COURIER_ID, BigDecimal.TEN))
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to reassign this order"
	}

	def "testAssignOrder"() {
		given:
		Order order = orderRepository.findAll().get(0)
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		AssignOrderForm assignment = new AssignOrderForm(COURIER_ID, BigDecimal.TEN)
		when:
		DetailsDto dto = orderService.assignOrder(auth, order.id, assignment)
		println dto
		then:
		1 * orderEventPublisher.sendEvent(_)
		verifyAll(dto) {
			dto.courierId == assignment.courierId
			dto.cost == assignment.cost
			dto.status == Status.ASSIGNED
		}
	}

	def "testAssignOrder - inactive courier"() {
		given:
		Order order = orderRepository.findAll().get(0)
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		AssignOrderForm assignment = new AssignOrderForm(23L, BigDecimal.TEN)
		when:
		orderService.assignOrder(auth, order.id, assignment)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "no active courier with id=" + assignment.courierId
	}

	def "testAssignOrder - not courier role"() {
		given:
		Order order = orderRepository.findAll().get(0)
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		AssignOrderForm assignment = new AssignOrderForm(24L, BigDecimal.TEN)
		when:
		orderService.assignOrder(auth, order.id, assignment)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "no active courier with id=" + assignment.courierId
	}

	def "testAssignOrder - non-existent courier"() {
		given:
		Order order = orderRepository.findAll().get(0)
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		AssignOrderForm assignment = new AssignOrderForm(25L, BigDecimal.TEN)
		when:
		orderService.assignOrder(auth, order.id, assignment)
		then:
		def exception = thrown(ResourceNotFoundException)
		exception.message == "no courier with id=" + assignment.courierId
	}

	def "testAssignOrder - error auth-service"() {
		given:
		Order order = orderRepository.findAll().get(0)
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		AssignOrderForm assignment = new AssignOrderForm(26L, BigDecimal.TEN)
		when:
		orderService.assignOrder(auth, order.id, assignment)
		then:
		def exception = thrown(RuntimeException)
		exception.message == "connection error"
	}

	def "testGetOrderDetailsById - non-existent order"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		when:
		orderService.getOrderDetailsById(auth, 200L)
		then:
		def exception = thrown(ResourceNotFoundException)
		exception.message == "order not found"
	}

	def "testGetOrderDetailsById - correct userId"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		DetailsDto dto = orderService.getOrderDetailsById(auth, orderId)
		println dto
		then:
		verifyAll(dto) {
			dto.id == orderId
			dto.userId == USER_ID
		}
	}

	def "testGetOrderDetailsById - incorrect userId"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, 5L)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		orderService.getOrderDetailsById(auth, orderId)
		then:
		def exception = thrown(OperationNotPermittedException)
		exception.message == "access denied"
	}

	def "testGetOrderDetailsById - admin access"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		DetailsDto dto = orderService.getOrderDetailsById(auth, orderId)
		println dto
		then:
		verifyAll(dto) {
			dto.id == orderId
			dto.userId == USER_ID
		}
	}

	def "testUpdateDestination - success"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		DetailsDto dto = orderService.updateDestination(auth, orderId, "Amsterdam")
		println dto
		then:
		verifyAll(dto) {
			dto.destination == "Amsterdam"
		}
	}

	def "testUpdateDestination - incorrect userId"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, 5L)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		orderService.updateDestination(auth, orderId, "Amsterdam")
		then:
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to change destination for this order"
	}

	def "testUpdateStatus - by user"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		orderService.updateStatus(auth, orderId, Status.DELIVERED)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to update status"
	}

	def "testUpdateStatus - improper assignment by admin"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		orderService.updateStatus(auth, orderId, Status.ASSIGNED)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to update status to ASSIGN, use assignOrder method"
	}

	def "testUpdateStatus - by admin"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_ADMIN, ADMIN_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		Status newStatus = Status.DELIVERED
		when:
		OrderDto dto = orderService.updateStatus(auth, orderId, newStatus)
		println dto
		then:
		1 * orderEventPublisher.sendEvent(_)
		verifyAll(dto) {
			dto.status == newStatus
		}
	}

	def "testUpdateStatus - by courier"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_COURIER, COURIER_ID)
		Order order = orderRepository.findAll().get(0)
		order.status = Status.ASSIGNED
		order.courierId = COURIER_ID
		orderRepository.save(order)
		Status newStatus = Status.SHIPPED
		when:
		OrderDto dto = orderService.updateStatus(auth, order.id, newStatus)
		println dto
		then:
		1 * orderEventPublisher.sendEvent(_)
		verifyAll(dto) {
			dto.status == newStatus
			dto.courierId == COURIER_ID
		}
	}

	def "testUpdateStatus - incorrect courierId by courier"() {
		given:
		Order order = orderRepository.findAll().get(0)
		order.status = Status.ASSIGNED
		order.courierId = COURIER_ID
		orderRepository.save(order)
		Authentication auth = makeAuthentication(Role.ROLE_COURIER, 9L)
		when:
		orderService.updateStatus(auth, order.id, Status.SHIPPED)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to update status"
	}

	def "testUpdateStatus - incorrect status by courier"() {
		given:
		Order order = orderRepository.findAll().get(0)
		order.status = Status.DELIVERED
		order.courierId = COURIER_ID
		orderRepository.save(order)
		Authentication auth = makeAuthentication(Role.ROLE_COURIER, COURIER_ID)
		when:
		orderService.updateStatus(auth, order.id, Status.SHIPPED)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to update status"
	}

	def testCreateOrder() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		UserOrderForm form = new UserOrderForm(null, "Novosibirsk", "London", "Vova", "89039003333")
		when:
		DetailsDto dto = orderService.createOrder(auth, form)
		println dto
		then:
		1 * orderEventPublisher.sendEvent(_)
		verifyAll(dto) {
			dto.id != null
			dto.userId == USER_ID
			dto.createdAt != null
		}
	}

	def "testCancelOrder - success"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		OrderDto dto = orderService.cancelOrder(auth, orderId)
		println dto
		then:
		1 * orderEventPublisher.sendEvent(_)
		verifyAll(dto) {
			dto.status == Status.CANCELLED
		}
	}

	def "testCancelOrder - incorrect userId"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, 5L)
		Long orderId = orderRepository.findAll().get(0).getId()
		when:
		orderService.cancelOrder(auth, orderId)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to cancel this order"
	}

	def "testCancelOrder - incorrect status"() {
		given:
		Authentication auth = makeAuthentication(Role.ROLE_USER, USER_ID)
		Order order = orderRepository.findAll().get(0)
		order.setStatus(Status.SHIPPED)
		orderRepository.save(order)
		when:
		orderService.cancelOrder(auth, order.id)
		then:
		0 * orderEventPublisher.sendEvent(_)
		def exception = thrown(OperationNotPermittedException)
		exception.message == "unable to cancel this order"
	}

	private static Authentication makeAuthentication(Role role, Long id) {
		Authentication auth = new UsernamePasswordAuthenticationToken(
				"name",null, List.of(new SimpleGrantedAuthority(role.name())))
		auth.setDetails(id)
		return auth
	}

	private static Order defaultOrder() {
		Order order = new Order()
		order.setStatus(Status.CREATED)
		order.setCreatedAt(new Date())
		order.setUserId(USER_ID)
		Details details = new Details()
		details.setDeparture("Novosibirsk")
		details.setDestination("Manchester")
		details.setRecipientName("Vova");
		details.setRecipientPhone("89039003333");
		order.setDetails(details);
		return order
	}

}
