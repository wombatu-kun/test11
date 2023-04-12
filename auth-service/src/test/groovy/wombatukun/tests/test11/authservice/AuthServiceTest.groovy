package wombatukun.tests.test11.authservice

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import wombatukun.tests.test11.authservice.security.AuthConfig

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Stepwise
class AuthServiceTest extends Specification {

	@Autowired
	private MockMvc mvc
	@Autowired
	private AuthConfig authConfig;
	@Shared
	private String accessToken;

	def "unauthorized for UserController.me"() {
		expect: "without authentication - 401"
		mvc
				.perform(get("/v1/auth"))
				.andExpect(status().isUnauthorized())
	}

	def "admin authentication"() {
		when:
			def response = mvc.perform(
					post("/oauth/token").
							with(httpBasic(authConfig.client, authConfig.secret))
							.param("grant_type", "password")
							.param("username", "admin@company.com")
							.param("password", "password")
			)
					.andReturn().response
			def content = new JsonSlurper().parseText(response.contentAsString)
			println content
			accessToken = (String) content.access_token
			println accessToken
		then:
			response.status == 200
			assert content instanceof Map
			content.access_token != null
			content.user_id == 1
	}

	def "ok for UserController.me"() {
		expect: "with authentication - 200"
		mvc
				.perform(get("/v1/auth").header("Authorization", "Bearer ${accessToken}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.content.user').value("admin@company.com"))
	}

	def testGetAnyUserById() {
		when:
			def response = mvc.perform(
					get("/v1/users/{id}", "1").header("Authorization", "Bearer ${accessToken}"))
					.andReturn().response
			def content = new JsonSlurper().parseText(response.contentAsString)
			println content
		then:
			content.content != null
			content.error == null
			content.content.id == 1
	}

}
