package wombatukun.tests.test11.orderservice.dao.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.orderservice.dao.projections.OrderCount;
import wombatukun.tests.test11.orderservice.dao.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Slice<Order> findByUserId(Long userId, Pageable pageable);
    Slice<Order> findByCourierId(Long courierId, Pageable pageable);

    @Query("SELECT o.status AS status, COUNT(o) AS count FROM Order AS o GROUP BY o.status ORDER BY o.status ASC")
    List<OrderCount> countTotalsByStatuses();

    /*@Modifying
    @Transactional
    @Query("update Order set status=:status where id=:id")
    void updateStatus(@Param("id") Long id, @Param("status") Status status);

    @Modifying
    @Transactional
    @Query("update Order set courierId=:courierId where id=:id")
    void updateCourier(@Param("id") Long id, @Param("courierId") Long courierId);*/


}
