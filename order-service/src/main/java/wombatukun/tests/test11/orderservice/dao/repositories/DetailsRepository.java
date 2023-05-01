package wombatukun.tests.test11.orderservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.orderservice.dao.entities.Details;


import jakarta.transaction.Transactional;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long> {

    @Modifying
    @Transactional
    @Query("update Details set destination=:destination where id=:id")
    void updateDestination(@Param("id") Long id, @Param("destination") String destination);

}
