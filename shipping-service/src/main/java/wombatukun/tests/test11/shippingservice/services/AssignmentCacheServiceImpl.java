package wombatukun.tests.test11.shippingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache;
import wombatukun.tests.test11.shippingservice.dao.repositories.AssignmentCacheRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentCacheServiceImpl implements AssignmentCacheService {

    private final AssignmentCacheRepository assignmentCacheRepository;

    public AssignmentCache getCache(Long orderId) {
        try {
            return assignmentCacheRepository.findById(orderId).orElse(null);
        } catch (Exception ex){
            log.error("Error trying to retrieve assignmentCache by orderId={}. Exception {}", orderId, ex);
            return null;
        }
    }

    public void deleteCache(Long orderId) {
        try {
            assignmentCacheRepository.deleteById(orderId);
        } catch (Exception ex){
            log.error("Unable to delete assignmentCache with orderId={}. Exception {}", orderId, ex);
        }
    }

    public void saveCache(AssignmentCache assignment) {
        try {
            assignmentCacheRepository.save(assignment);
        } catch (Exception ex){
            log.error("Unable to cache assignment with orderId={}. Exception {}", assignment.getOrderId(), ex);
        }
    }
}
