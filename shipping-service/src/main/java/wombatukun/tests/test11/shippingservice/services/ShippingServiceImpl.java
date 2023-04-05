package wombatukun.tests.test11.shippingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.common.security.AuthInfo;
import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache;
import wombatukun.tests.test11.shippingservice.dao.entities.Shipping;
import wombatukun.tests.test11.shippingservice.dao.repositories.ShippingRepository;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;
import wombatukun.tests.test11.shippingservice.dto.ShippingForm;
import wombatukun.tests.test11.shippingservice.mappers.ShippingMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;
    private final CacheService cacheService;

    @Override
    public void addShipping(Authentication authentication, ShippingForm dto) {
        AuthInfo authInfo = AuthInfo.fromAuthentication(authentication);
        Set<Shipping> shippingSet = new HashSet<>();
        for (Long orderId: dto.getOrderIds()) {
            AssignmentCache assignmentCache = cacheService.getCache(orderId);
            if (assignmentCache != null && authInfo.getId().equals(assignmentCache.getCourierId())) {
                shippingSet.add(shippingMapper.mapFormToEntity(orderId, authInfo.getId(), dto));
            }
        }
        if (!shippingSet.isEmpty()) {
            shippingRepository.saveAllAndFlush(shippingSet);
        }
    }

    @Override
    public List<ShippingDto> getShippingTrack(Long orderId) {
        return shippingRepository.findAllByOrderIdOrOrderByWasAt(orderId).stream()
                .map(shippingMapper::mapEntityToDto).collect(Collectors.toList());
    }

}
