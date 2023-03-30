package wombatukun.tests.test11.authservice.services;

import org.springframework.security.core.Authentication;
import wombatukun.tests.test11.authservice.dto.UserDto;
import wombatukun.tests.test11.authservice.dto.UserForm;
import wombatukun.tests.test11.authservice.enums.Role;
import wombatukun.tests.test11.authservice.dao.projections.UserCount;

import java.util.List;

public interface UserService {
    UserDto register(UserForm form);
    UserDto suspend(Authentication authentication, Long id);
    UserDto getById(Long id);
    List<UserCount> countTotalsByRoles();
    Long count(Role role);
}
