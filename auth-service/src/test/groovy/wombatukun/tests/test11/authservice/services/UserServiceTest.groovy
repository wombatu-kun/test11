package wombatukun.tests.test11.authservice.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification
import spock.lang.Stepwise
import wombatukun.tests.test11.authservice.dao.projections.UserCount
import wombatukun.tests.test11.authservice.dao.repositories.UserRepository
import wombatukun.tests.test11.authservice.dto.UserDto
import wombatukun.tests.test11.authservice.dto.UserForm
import wombatukun.tests.test11.authservice.enums.Role
import wombatukun.tests.test11.authservice.enums.Status
import wombatukun.tests.test11.authservice.mappers.UserMapper
import wombatukun.tests.test11.authservice.messaging.UserEventPublisher
import wombatukun.tests.test11.authservice.services.UserService
import wombatukun.tests.test11.authservice.services.UserServiceImpl
import wombatukun.tests.test11.common.exceptions.OperationNotPermittedException
import wombatukun.tests.test11.common.exceptions.RegistrationNotAllowedException
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException

@SpringBootTest
@Stepwise
class UserServiceTest extends Specification {

	@Autowired
	private UserRepository userRepository
	@Autowired
	private UserMapper userMapper
	private UserEventPublisher userEventPublisher = Mock()

	def testRegisterSuccess() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
			UserForm form = new UserForm("user1", "user1@mail.com", "pw", Role.ROLE_USER);
		when:
			UserDto result = userService.register(form)
			println result
		then:
			1 * userEventPublisher.sendEvent(_)
			assert (userRepository.findById(result.id).isPresent())
	}

	def testRegisterConflict() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
			UserForm form = new UserForm("user2", "admin@company.com", "pw", Role.ROLE_USER);
		when:
			userService.register(form)
		then:
			def exception = thrown(RegistrationNotAllowedException)
			exception.message == 'Conflict email: ' + form.email
	}

	def testGetUserByIdSuccess() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
		when:
			UserDto result = userService.getById(1L)
		then:
			result != null
	}

	def testGetUserByIdNotFound() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
		when:
			userService.getById(502L)
		then:
			def exception = thrown(ResourceNotFoundException)
			exception.message == 'user not found'
	}

	def testCount(Role role, Long count) {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
		expect:
			userService.count(role) == count
		where:
			count | role
			1     | Role.ROLE_ADMIN
			0     | Role.ROLE_COURIER
	}

	def countTotalsByRoles() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
		when:
			List<UserCount> result = userService.countTotalsByRoles()
		then:
			!result.isEmpty()
			result.size() == 2
			result.get(0).role == Role.ROLE_ADMIN
			result.get(0).count == 1
			result.get(1).role == Role.ROLE_USER
			result.get(1).count == 1
	}

	def testUpdateStatusSuccess() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
			Authentication auth = new UsernamePasswordAuthenticationToken(
				"admin@company.com", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
			)
		when:
			UserDto result = userService.updateStatus(auth, 2L, Status.SUSPENDED)
			println result
		then:
		1 * userEventPublisher.sendEvent(_)
		result.id == 2L
		result.getStatus() == Status.SUSPENDED
	}

	def testUpdateStatusFailure() {
		given:
			UserService userService = new UserServiceImpl(userRepository, userMapper, userEventPublisher)
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
