package wombatukun.tests.test11.authservice.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wombatukun.tests.test11.authservice.dao.entities.User;
import wombatukun.tests.test11.authservice.dao.projections.UserCount;
import wombatukun.tests.test11.common.security.Role;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    Long countAllByRole(Role role);
    @Query("SELECT u.role AS role, COUNT(u) AS count FROM User AS u GROUP BY u.role ORDER BY u.role ASC")
    List<UserCount> countTotalsByRoles();

}
