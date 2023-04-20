package wombatukun.tests.test11.shippingservice.services

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject
import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache
import wombatukun.tests.test11.shippingservice.dao.entities.Shipping
import wombatukun.tests.test11.shippingservice.dao.repositories.ShippingRepository
import wombatukun.tests.test11.shippingservice.dto.ShippingDto
import wombatukun.tests.test11.shippingservice.dto.ShippingForm

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.stream.Collectors

@SpringBootTest
@Stepwise
class ShippingServiceTest extends Specification {

	@Autowired
	private ShippingRepository shippingRepository
	@SpringBean
	private AssignmentCacheService cacheService = Stub() {
		getCache(70) >> new AssignmentCache(70L, 2L)
		getCache(71) >> null
		getCache(72) >> new AssignmentCache(72L, 3L)
		getCache(73) >> new AssignmentCache(73L, 2L)
	}
	@Subject
	@Autowired
	private ShippingService shippingService;

	def testAddShipping() {
		given:
		Date date1 = Date.from(LocalDateTime.of(2023, 4, 1, 12, 1).toInstant(ZoneOffset.UTC))
		Date date2 = Date.from(LocalDateTime.of(2023, 4, 1, 12, 2).toInstant(ZoneOffset.UTC))
		Date date3 = Date.from(LocalDateTime.of(2023, 4, 1, 12, 3).toInstant(ZoneOffset.UTC))
		Long courierId = 2L
		ShippingForm coord1 = new ShippingForm(Set.of(70L, 71L, 72L), date1, 1d, 2d)
		ShippingForm coord2 = new ShippingForm(Set.of(70L, 71L, 72L), date2, 2d, 3d)
		ShippingForm coord3 = new ShippingForm(Set.of(70L, 71L, 72L, 73L), date3, 3d, 4d)
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				"name",null, List.of(new SimpleGrantedAuthority("ROLE_COURIER")))
		auth.setDetails(courierId)
		when:
		shippingService.addShipping(auth, coord1)
		shippingService.addShipping(auth, coord2)
		shippingService.addShipping(auth, coord3)
		List<Shipping> result = shippingRepository.findAll().stream()
				.sorted(Comparator.comparingLong(Shipping::getOrderId)).collect(Collectors.toList())
		println result
		then:
		verifyAll(result) {
			result.size() == 4
			result[0].courierId == courierId
			result[0].orderId == 70L
			result[0].latitude == 1d
			result[0].longitude == 2d
			result[1].courierId == courierId
			result[1].orderId == 70L
			result[1].latitude == 2d
			result[1].longitude == 3d
			result[2].courierId == courierId
			result[2].orderId == 70L
			result[2].latitude == 3d
			result[2].longitude == 4d
			result[3].courierId == courierId
			result[3].orderId == 73L
			result[3].latitude == 3d
			result[3].longitude == 4d
		}
	}

	def testGetShippingTrack() {
		given:
		Long order1 = 70L
		Long order2 = 72L
		Long order3 = 73L
		when:
		List<ShippingDto> order1Track = shippingService.getShippingTrack(order1)
		List<ShippingDto> order2Track = shippingService.getShippingTrack(order2)
		List<ShippingDto> order3Track = shippingService.getShippingTrack(order3)
		then:
		order1Track.size() == 3
		order1Track[0].latitude == 1d
		order1Track[1].latitude == 2d
		order1Track[2].latitude == 3d
		order2Track.isEmpty()
		order3Track.size() == 1
		order3Track[0].latitude == 3d
	}

}
