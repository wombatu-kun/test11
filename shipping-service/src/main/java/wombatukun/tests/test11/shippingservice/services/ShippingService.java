package wombatukun.tests.test11.shippingservice.services;

import org.springframework.security.core.Authentication;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;

import java.util.List;

public interface ShippingService {

    List<ShippingDto> getShippingTrack(Authentication authentication, Long orderId);
    void addShipping(Authentication authentication, ShippingDto dto);

}
