package wombatukun.tests.test11.orderservice.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.orderservice.dao.entities.UserCache;

@Repository
public interface UserCacheRepository extends CrudRepository<UserCache, Long> {
}
