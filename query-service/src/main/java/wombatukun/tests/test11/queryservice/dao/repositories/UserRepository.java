package wombatukun.tests.test11.queryservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.queryservice.dao.entities.User;
import wombatukun.tests.test11.queryservice.dao.projections.UserStat;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.id AS userId, u.name AS name, u.email AS email, u.status AS status, " +
            "u.createdAt AS createdAt, u.suspendedAt AS suspendedAt, u.deletedAt AS deletedAt, " +
            "SUM( CASE WHEN (o.status = 'DELIVERED') THEN 1 ELSE 0 END ) AS countDelivered, " +
            "SUM( CASE WHEN (o.status = 'CREATED' OR o.status = 'ASSIGNED' OR o.status = 'SHIPPED') THEN 1 ELSE 0 END ) AS countInProgress, " +
            "COUNT(o) AS countTotal, " +
            "SUM( CASE WHEN (o.status = 'DELIVERED') THEN o.cost ELSE 0 END ) AS totalProfit " +
            "FROM User u LEFT JOIN u.usersOrders o WHERE u.role = 'ROLE_USER' GROUP BY u.id ORDER BY u.id")
    List<UserStat> getUsersStats();

    @Query("SELECT u.id AS userId, u.name AS name, u.email AS email, u.status AS status, " +
            "u.createdAt AS createdAt, u.suspendedAt AS suspendedAt, u.deletedAt AS deletedAt, " +
            "SUM( CASE WHEN (o.status = 'DELIVERED') THEN 1 ELSE 0 END ) AS countDelivered, " +
            "SUM( CASE WHEN (o.status = 'ASSIGNED' OR o.status = 'SHIPPED') THEN 1 ELSE 0 END ) AS countInProgress, " +
            "COUNT(o) AS countTotal, " +
            "SUM( CASE WHEN (o.status = 'DELIVERED') THEN o.cost ELSE 0 END ) AS totalProfit " +
            "FROM User u LEFT JOIN u.couriersOrders o WHERE u.role = 'ROLE_COURIER' GROUP BY u.id ORDER BY u.id")
    List<UserStat> getCouriersStats();

}
