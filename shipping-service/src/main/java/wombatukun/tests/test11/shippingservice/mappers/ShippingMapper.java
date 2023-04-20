package wombatukun.tests.test11.shippingservice.mappers;

import wombatukun.tests.test11.shippingservice.dao.entities.Shipping;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;
import wombatukun.tests.test11.shippingservice.dto.ShippingForm;

public interface ShippingMapper {

    ShippingDto mapEntityToDto(Shipping shipping);
    Shipping mapFormToEntity(Long orderId, Long courierId, ShippingForm form);

}
