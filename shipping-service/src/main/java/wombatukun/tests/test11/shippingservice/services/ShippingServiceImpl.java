package wombatukun.tests.test11.shippingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.shippingservice.dao.repositories.ShippingRepository;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;

    @Override
    public List<ShippingDto> getShippingTrack(Authentication authentication, Long orderId) {
        return null;
    }

    @Override
    public void addShipping(Authentication authentication, ShippingDto dto) {

    }
}
