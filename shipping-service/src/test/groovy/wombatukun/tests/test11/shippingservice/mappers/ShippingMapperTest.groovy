package wombatukun.tests.test11.shippingservice.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import wombatukun.tests.test11.shippingservice.dao.entities.Shipping
import wombatukun.tests.test11.shippingservice.dto.ShippingDto
import wombatukun.tests.test11.shippingservice.dto.ShippingForm

@SpringBootTest
class ShippingMapperTest extends Specification {

	@Subject
	@Autowired
	private ShippingMapper shippingMapper

	def testMapEntityToDto() {
		given:
			Shipping shipping = new Shipping(1L, new Date(), 7L, 4d, 2d)
		when:
			ShippingDto dto = shippingMapper.mapEntityToDto(shipping)
			println dto
		then:
			verifyAll(dto) {
				dto.courierId == shipping.courierId
				dto.wasAt == shipping.wasAt
				dto.latitude == shipping.latitude
				dto.longitude == shipping.longitude
			}
	}

	def testMapFormToEntity() {
		given:
			Long orderId = 34L
			Long courierId = 55L
			ShippingForm form = new ShippingForm(Set.of(orderId, 99L), new Date(), 8d, 12d)
		when:
			Shipping shipping = shippingMapper.mapFormToEntity(orderId, courierId, form)
			println shipping
		then:
		verifyAll(shipping) {
			shipping.orderId == orderId
			shipping.courierId == courierId
			shipping.wasAt == form.wasAt
			shipping.latitude == form.latitude
			shipping.longitude == form.longitude
		}
	}

}
