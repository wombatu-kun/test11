package wombatukun.tests.test11.queryservice.services;

import wombatukun.tests.test11.common.messaging.UserEvent;
import wombatukun.tests.test11.queryservice.dao.entities.User;
import wombatukun.tests.test11.queryservice.dto.UserStatDto;

import java.util.List;

public interface UserService {

    User findUserById(Long userId);
    List<UserStatDto> getUsersStats();
    List<UserStatDto> getCouriersStats();
    void handleEvent(UserEvent event);

}
