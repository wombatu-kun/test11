package wombatukun.tests.test11.queryservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.queryservice.dao.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
