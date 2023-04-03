package wombatukun.tests.test11.shippingservice.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.shippingservice.dao.entities.AssignmentCache;

@Repository
public interface CacheRepository extends CrudRepository<AssignmentCache, Long> {
}
