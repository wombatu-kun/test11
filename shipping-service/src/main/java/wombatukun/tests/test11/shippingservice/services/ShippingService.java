package wombatukun.tests.test11.shippingservice.services;

import org.springframework.security.core.Authentication;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;
import wombatukun.tests.test11.shippingservice.dto.ShippingForm;

import java.util.List;

public interface ShippingService {

    void addShipping(Authentication authentication, ShippingForm dto);
    List<ShippingDto> getShippingTrack(Long orderId);

}
