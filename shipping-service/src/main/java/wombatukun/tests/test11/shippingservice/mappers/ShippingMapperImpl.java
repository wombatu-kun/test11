package wombatukun.tests.test11.shippingservice.mappers;

import org.springframework.stereotype.Component;
import wombatukun.tests.test11.shippingservice.dao.entities.Shipping;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;
import wombatukun.tests.test11.shippingservice.dto.ShippingForm;

@Component
public class ShippingMapperImpl implements ShippingMapper {

    @Override
    public ShippingDto mapEntityToDto(Shipping shipping) {
        return ShippingDto.builder()
                .courierId(shipping.getCourierId())
                .wasAt(shipping.getWasAt())
                .latitude(shipping.getLatitude())
                .longitude(shipping.getLongitude())
                .build();
    }

    @Override
    public Shipping mapFormToEntity(Long orderId, Long courierId, ShippingForm form) {
        Shipping shipping = new Shipping();
        shipping.setOrderId(orderId);
        shipping.setCourierId(courierId);
        shipping.setWasAt(form.getWasAt());
        shipping.setLatitude(form.getLatitude());
        shipping.setLongitude(form.getLongitude());
        return null;
    }
}
