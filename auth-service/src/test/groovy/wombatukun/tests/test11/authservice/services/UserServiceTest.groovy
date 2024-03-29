package wombatukun.tests.test11.authservice.services

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject
import wombatukun.tests.test11.authservice.dao.projections.UserCount
import wombatukun.tests.test11.authservice.dao.repositories.UserRepository
import wombatukun.tests.test11.authservice.dto.UserDto
import wombatukun.tests.test11.authservice.dto.UserForm
import wombatukun.tests.test11.authservice.enums.Status
import wombatukun.tests.test11.authservice.messaging.UserEventPublisher
import wombatukun.tests.test11.common.exceptions.OperationNotPermittedException
import wombatukun.tests.test11.common.exceptions.RegistrationNotAllowedException
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException
import wombatukun.tests.test11.common.security.Role

@SpringBootTest
@Stepwise
class UserServiceTest extends Specification {

	@Autowired
	private UserRepository userRepository
	@SpringBean
	private UserEventPublisher userEventPublisher = Mock()
	@Subject
	@Autowired
	private UserService userService

	def testRegisterSuccess() {
		given:
		UserForm form = new UserForm("user1", "user1@mail.com", "pw", Role.ROLE_USER)
		when:
		UserDto result = userService.register(form)
		println result
		then:
		1 * userEventPublisher.sendEvent(_)
		assert (userRepository.findById(result.id).isPresent())
	}

	def testRegisterConflict() {
		given:
		UserForm form = new UserForm("user2", "admin@company.com", "pw", Role.ROLE_USER)
		when:
		userService.register(form)
		then:
		def exception = thrown(RegistrationNotAllowedException)
		exception.message == 'Conflict email: ' + form.email
	}

	def testGetUserByIdSuccess() {
		when:
		UserDto result = userService.getById(1L)
		then:
		result != null
	}

	def testGetUserByIdNotFound() {
		when:
		userService.getById(502L)
		then:
		def exception = thrown(ResourceNotFoundException)
		exception.message == 'user not found'
	}

	def testCount(Role role, Long count) {
		expect:
		userService.count(role) == count
		where:
		count | role
		1     | Role.ROLE_ADMIN
		0     | Role.ROLE_COURIER
	}

	def countTotalsByRoles() {
		when:
		List<UserCount> result = userService.countTotalsByRoles()
		then:
		verifyAll(result) {
			!result.isEmpty()
			result.size() == 2
			result.get(0).role == Role.ROLE_ADMIN
			result.get(0).count == 1
			result.get(1).role == Role.ROLE_USER
			result.get(1).count == 1
		}
	}

	def testUpdateStatusSuccess() {
		given:
		Authentication auth = new UsernamePasswordAuthenticationToken(
				"admin@company.com", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
		)
		when:
		UserDto result = userService.updateStatus(auth, 2L, Status.SUSPENDED)
		println result
		then:
		1 * userEventPublisher.sendEvent(_)
		verifyAll(result) {
			result.id == 2L
			result.getStatus() == Status.SUSPENDED
		}
	}

	def testUpdateStatusFailure() {
		given:
		Authentication auth = new UsernamePasswordAuthenticationToken(
				"admin@company.com", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
		)
		when:
		userService.updateStatus(auth, 1L, Status.SUSPENDED)
		then:
		def exception = thrown(OperationNotPermittedException)
		exception.message == 'unable to update your own status'
	}

}
