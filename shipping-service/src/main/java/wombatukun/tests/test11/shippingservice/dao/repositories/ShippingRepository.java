package wombatukun.tests.test11.shippingservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.shippingservice.dao.entities.Shipping;
import wombatukun.tests.test11.shippingservice.dao.entities.ShippingId;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, ShippingId> {

    @Query("select s from Shipping s where s.orderId = :orderId order by s.wasAt asc")
    List<Shipping> findAllByOrderIdOrOrderByWasAt(Long orderId);

}
