package wombatukun.tests.test11.shippingservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.shippingservice.dao.entities.Shipping;
import wombatukun.tests.test11.shippingservice.dao.entities.ShippingId;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, ShippingId> {

}
