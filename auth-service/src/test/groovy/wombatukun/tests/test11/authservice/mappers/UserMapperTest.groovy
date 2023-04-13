package wombatukun.tests.test11.authservice.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.authservice.dao.entities.User
import wombatukun.tests.test11.authservice.dto.UserDto
import wombatukun.tests.test11.authservice.dto.UserForm
import wombatukun.tests.test11.authservice.enums.Role
import wombatukun.tests.test11.authservice.enums.Status
import wombatukun.tests.test11.authservice.mappers.UserMapper
import wombatukun.tests.test11.common.messaging.UserEvent

@SpringBootTest
class UserMapperTest extends Specification {

	@Subject
	@Autowired
	private UserMapper userMapper

	private User user = new User(502L, new Date(), "user1", "user1@mail.com", "pw", Role.ROLE_USER, Status.SUSPENDED)

	def testMapFormToEntity() {
		given:
			UserForm form = new UserForm("user1", "user1@mail.com", "pw", Role.ROLE_USER)
		when:
			User user = userMapper.mapFormToEntity(form)
			println user
		then:
			verifyAll(user) {
				user.createdAt != null
				user.password != null
				user.password != form.password
				user.status == Status.ACTIVE
				user.name == form.name
				user.email == form.email
				user.role == form.role
			}
	}

	def testMapEntityToDto() {
		when:
			UserDto dto = userMapper.mapEntityToDto(user)
			println dto
		then:
			verifyAll(dto) {
				dto.id == user.id
				dto.createdAt == user.createdAt
				dto.password == null
				dto.status == user.status
				dto.name == user.name
				dto.email == user.email
				dto.role == user.role
			}
	}

	def testMapEntityToEvent() {
		when:
			UserEvent event = userMapper.mapEntityToEvent(user)
			println event
		then:
			event.type == UserEvent.class.getTypeName()
			event.id == user.getId()
			event.timestamp != null
			event.status.name() == user.status.name()
			event.name == user.name
			event.email == user.email
			event.role.name() == user.role.name()
	}

}
