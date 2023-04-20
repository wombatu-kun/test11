package wombatukun.tests.test11.authservice.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.authservice.dto.UserDto;
import wombatukun.tests.test11.authservice.dao.entities.User;
import wombatukun.tests.test11.authservice.dto.UserForm;
import wombatukun.tests.test11.authservice.enums.Status;
import wombatukun.tests.test11.common.security.Role;
import wombatukun.tests.test11.common.usercontext.UserContext;
import wombatukun.tests.test11.common.enums.UserStatus;
import wombatukun.tests.test11.common.messaging.UserEvent;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User mapFormToEntity(UserForm form) {
        User user = new User();
        user.setCreatedAt(new Date());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(form.getRole());
        user.setStatus(Status.ACTIVE);
        return user;
    }

    @Override
    public UserDto mapEntityToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }

    @Override
    public UserEvent mapEntityToEvent(User user) {
        return UserEvent.builder()
                .type(UserEvent.class.getTypeName())
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(Role.valueOf(user.getRole().name()))
                .status(UserStatus.valueOf(user.getStatus().name()))
                .timestamp(new Date())
                .correlationId(UserContext.getCorrelationId())
                .build();
    }
}
