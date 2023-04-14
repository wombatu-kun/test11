package wombatukun.tests.test11.orderservice.services;

import wombatukun.tests.test11.orderservice.dao.entities.UserCache;

public interface UserService {

    void saveUserCache(UserCache cache);
    UserCache getUser(Long id);

}
