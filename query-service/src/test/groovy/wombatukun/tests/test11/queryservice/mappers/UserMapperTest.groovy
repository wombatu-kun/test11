package wombatukun.tests.test11.queryservice.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.common.enums.UserStatus
import wombatukun.tests.test11.common.messaging.UserEvent
import wombatukun.tests.test11.common.security.Role
import wombatukun.tests.test11.queryservice.dao.entities.User
import wombatukun.tests.test11.queryservice.dao.projections.UserStat
import wombatukun.tests.test11.queryservice.dto.UserStatDto

@SpringBootTest
class UserMapperTest extends Specification {

	@Subject
	@Autowired
	private UserMapper userMapper

	def testMapProjectionToDto() {
		given:
		UserStat projection = new UserStat() {
			@Override Long getUserId() { return 1L }
			@Override String getName() { return "name1" }
			@Override String getEmail() { return "email@gmail.com" }
			@Override UserStatus getStatus() { return UserStatus.ACTIVE }
			@Override Date getCreatedAt() { return new Date() }
			@Override Date getSuspendedAt() { return null }
			@Override Date getDeletedAt() { return null	}
			@Override Long getCountDelivered() { return 1L }
			@Override Long getCountInProgress() { return 2L }
			@Override Long getCountTotal() { return 3L }
			@Override BigDecimal getTotalProfit() {	return BigDecimal.valueOf(4000L)	}
		}
		when:
		UserStatDto dto = userMapper.mapProjectionToDto(projection)
		println dto
		then:
		verifyAll(dto) {
			dto.userId == projection.userId
			dto.name == projection.name
			dto.email == projection.email
			dto.status == projection.status
			dto.createdAt != null
			dto.suspendedAt == null
			dto.deletedAt == null
			dto.countDelivered == projection.countDelivered
			dto.countInProgress == projection.countInProgress
			dto.countTotal == projection.countTotal
			dto.totalProfit == projection.totalProfit
		}
	}

	def testMapEventToEntity() {
		given:
		UserEvent event = new UserEvent(UserEvent.class.name, 1L, "name1", "email@gmail.com",
				Role.ROLE_USER, UserStatus.ACTIVE, new Date(), "cor-id")
		when:
		User user = userMapper.mapEventToEntity(event, null)
		println user
		then:
		verifyAll(user) {
			event.id == user.id
			event.timestamp == user.createdAt
			event.name == user.name
			event.email == user.email
			event.status == user.status
			event.role == user.role
		}
	}

}
