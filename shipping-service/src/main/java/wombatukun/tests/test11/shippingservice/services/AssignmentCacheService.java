package wombatukun.tests.test11.shippingservice.services;

import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache;

public interface AssignmentCacheService {

    void saveCache(AssignmentCache cache);
    void deleteCache(Long orderId);
    AssignmentCache getCache(Long orderId);

}
