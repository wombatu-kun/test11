package wombatukun.tests.test11.shippingservice.mappers;

import wombatukun.tests.test11.shippingservice.dao.entities.Shipping;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;

public interface ShippingMapper {

    ShippingDto mapEntityToDto(Shipping shipping);
    Shipping mapDtoToEntity(ShippingDto dto);

}
