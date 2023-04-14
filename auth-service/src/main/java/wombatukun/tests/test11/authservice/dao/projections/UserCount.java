package wombatukun.tests.test11.authservice.dao.projections;

import wombatukun.tests.test11.common.security.Role;

public interface UserCount {
    Role getRole();
    Long getCount();
}
