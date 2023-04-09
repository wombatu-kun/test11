package wombatukun.tests.test11.queryservice.mappers;

import wombatukun.tests.test11.common.messaging.UserEvent;
import wombatukun.tests.test11.queryservice.dao.entities.User;
import wombatukun.tests.test11.queryservice.dao.projections.UserStat;
import wombatukun.tests.test11.queryservice.dto.UserStatDto;

public interface UserMapper {

    UserStatDto mapProjectionToDto(UserStat userStat);
    User mapEventToEntity(UserEvent event);

}
