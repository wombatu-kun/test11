package wombatukun.tests.test11.orderservice.services

import brave.Tracer
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject
import wombatukun.tests.test11.common.dto.CommonResponse
import wombatukun.tests.test11.common.enums.UserStatus
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException
import wombatukun.tests.test11.common.security.Role
import wombatukun.tests.test11.orderservice.dao.entities.UserCache
import wombatukun.tests.test11.orderservice.dao.repositories.UserCacheRepository
import wombatukun.tests.test11.orderservice.dto.UserDto

@SpringBootTest
@Stepwise
class UserServiceTest extends Specification {

	@SpringBean
	private UserCacheRepository userCacheRepository = Stub() {
		findById(1L) >> Optional.of(new UserCache(1L, Role.ROLE_COURIER, UserStatus.ACTIVE))
		findById(2L) >> Optional.empty()
		findById(3L) >> Optional.empty()
		findById(4L) >> Optional.empty()
		findById(5L) >> Optional.empty()
	}
	@SpringBean
	private RestTemplate restTemplate = Stub() {
		exchange("http://localhost:8083/v1/users/2", HttpMethod.GET, null, new ParameterizedTypeReference<CommonResponse<UserDto>>(){}) >>
				{ throw new ResourceAccessException("connection refused") }
		exchange("http://localhost:8083/v1/users/3", HttpMethod.GET, null, new ParameterizedTypeReference<CommonResponse<UserDto>>(){}) >>
				ResponseEntity.notFound().build()
		exchange("http://localhost:8083/v1/users/4", HttpMethod.GET, null, new ParameterizedTypeReference<CommonResponse<UserDto>>(){}) >>
				ResponseEntity.internalServerError().build()
		exchange("http://localhost:8083/v1/users/5", HttpMethod.GET, null, new ParameterizedTypeReference<CommonResponse<UserDto>>(){}) >>
				ResponseEntity.ok()
						.body(CommonResponse.success(new UserDto(5L, "name", "email", null, Role.ROLE_COURIER, UserStatus.ACTIVE, new Date())))
	}
	@SpringBean
	private Tracer tracer = Mock()

	@Subject
	@Autowired
	private UserService userService

	def "testGetUser - from cache"() {
		given:
		Long userId = 1L
		when:
		UserCache user = userService.getUser(userId)
		then:
		1 * tracer.startScopedSpan(_)
		verifyAll(user) {
			user.id == 1L
			user.role == Role.ROLE_COURIER
			user.status == UserStatus.ACTIVE
		}
	}

	def "testGetUser - auth-service unavailable"() {
		given:
		Long userId = 2L
		when:
		userService.getUser(userId)
		then:
		2 * tracer.startScopedSpan(_)
		def exception = thrown(RuntimeException)
		exception.message == "connection to auth-service refused"
	}

	def "testGetUser - user in auth-service not found"() {
		given:
		Long userId = 3L
		when:
		userService.getUser(userId)
		then:
		2 * tracer.startScopedSpan(_)
		def exception = thrown(ResourceNotFoundException)
		exception.message == "no courier with id=" + userId
	}

	def "testGetUser - error from auth-service"() {
		given:
		Long userId = 4L
		when:
		userService.getUser(userId)
		then:
		2 * tracer.startScopedSpan(_)
		def exception = thrown(RuntimeException)
		assert exception.message.startsWith("error response form auth-service:")
	}

	def "testGetUser - ok from auth-service"() {
		given:
		Long userId = 5L
		when:
		UserCache user = userService.getUser(userId)
		println user
		then:
		3 * tracer.startScopedSpan(_)
		verifyAll(user) {
			user != null
			user.id == userId
			user.role == Role.ROLE_COURIER
		}
	}

}
