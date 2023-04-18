package wombatukun.tests.test11.queryservice.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject
import wombatukun.tests.test11.common.enums.UserStatus
import wombatukun.tests.test11.common.messaging.UserEvent
import wombatukun.tests.test11.common.security.Role
import wombatukun.tests.test11.queryservice.dao.entities.User
import wombatukun.tests.test11.queryservice.dao.repositories.UserRepository
import wombatukun.tests.test11.queryservice.dto.UserStatDto

@SpringBootTest
@Stepwise
class UserServiceTest extends Specification {

	@Subject
	@Autowired
	private UserService userService
	@Autowired
	private UserRepository userRepository

	def testGetUsersStats() {
		when:
		List<UserStatDto> stats = userService.getUsersStats()
		println "UsersStats: ${stats}"
		then:
		verifyAll(stats) {
			stats != null
			stats.size() == 1
			stats[0].userId == 2L
			stats[0].name == "user1"
			stats[0].email == "user1@mail.com"
			stats[0].status == UserStatus.ACTIVE
			stats[0].countDelivered == 1L
			stats[0].countInProgress == 2L
			stats[0].countTotal == 4L
			stats[0].totalProfit == 500L
		}
	}

	def testGetCouriersStats() {
		when:
		List<UserStatDto> stats = userService.getCouriersStats()
		println "CouriersStats: ${stats}"
		then:
		verifyAll(stats) {
			stats != null
			stats.size() == 2
			stats[0].userId == 3L
			stats[0].name == "courier1"
			stats[0].email == "courier1@mail.com"
			stats[0].status == UserStatus.ACTIVE
			stats[0].countDelivered == 1L
			stats[0].countInProgress == 1L
			stats[0].countTotal == 2L
			stats[0].totalProfit == 500L
			stats[1].userId == 4L
		}
	}

	def testHandleEvent() {
		given:
		UserEvent event = new UserEvent(UserEvent.class.name, 4L, "courier2", "courier2@mail.com",
				Role.ROLE_COURIER, UserStatus.SUSPENDED, new Date(), "cor-id")
		when:
		userService.handleEvent(event)
		User user = userRepository.findById(event.id).orElse(null)
		then:
		verifyAll(user) {
			user != null
			user.status == event.status
			user.suspendedAt == event.timestamp
		}
	}

}
