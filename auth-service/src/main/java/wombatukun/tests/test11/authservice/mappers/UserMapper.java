package wombatukun.tests.test11.authservice.mappers;

import wombatukun.tests.test11.authservice.dto.UserDto;
import wombatukun.tests.test11.authservice.dao.entities.User;
import wombatukun.tests.test11.authservice.dto.UserForm;
import wombatukun.tests.test11.common.messaging.UserEvent;

public interface UserMapper {

    User mapFormToEntity(UserForm form);
    UserDto mapEntityToDto(User user);
    UserEvent mapEntityToEvent(User user);

}
