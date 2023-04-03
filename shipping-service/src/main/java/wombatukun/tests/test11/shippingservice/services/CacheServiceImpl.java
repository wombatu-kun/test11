package wombatukun.tests.test11.shippingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache;
import wombatukun.tests.test11.shippingservice.dao.repositories.CacheRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheRepository cacheRepository;

    public AssignmentCache getCache(Long orderId) {
        try {
            return cacheRepository.findById(orderId).orElse(null);
        } catch (Exception ex){
            log.error("Error trying to retrieve assignment by orderId={}. Exception {}", orderId, ex);
            return null;
        }
    }

    public void deleteCache(Long orderId) {
        try {
            cacheRepository.deleteById(orderId);
        } catch (Exception ex){
            log.error("Unable to delete assignment with orderId={}. Exception {}", orderId, ex);
        }
    }

    public void saveCache(AssignmentCache assignment) {
        try {
            cacheRepository.save(assignment);
        } catch (Exception ex){
            log.error("Unable to cache assignment with orderId={}. Exception {}", assignment.getOrderId(), ex);
        }
    }
}
