package wombatukun.tests.test11.orderservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.orderservice.dao.entities.Order;
import wombatukun.tests.test11.orderservice.dao.projections.OrderCount;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT o.status AS status, COUNT(o) AS count FROM Order AS o GROUP BY o.status ORDER BY o.status ASC")
    List<OrderCount> countTotalsByStatuses();

}
