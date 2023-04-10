package wombatukun.tests.test11.queryservice.mappers;

import org.springframework.stereotype.Component;
import wombatukun.tests.test11.common.messaging.UserEvent;
import wombatukun.tests.test11.queryservice.dao.entities.User;
import wombatukun.tests.test11.queryservice.dao.projections.UserStat;
import wombatukun.tests.test11.queryservice.dto.UserStatDto;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserStatDto mapProjectionToDto(UserStat proj) {
        return UserStatDto.builder()
                .userId(proj.getUserId())
                .name(proj.getName())
                .email(proj.getEmail())
                .status(proj.getStatus())
                .countDelivered(proj.getCountDelivered())
                .countInProgress(proj.getCountInProgress())
                .countTotal(proj.getCountTotal())
                .totalProfit(proj.getTotalProfit())
                .createdAt(proj.getCreatedAt())
                .suspendedAt(proj.getSuspendedAt())
                .deletedAt(proj.getDeletedAt())
                .build();
    }

    @Override
    public User mapEventToEntity(UserEvent event, User user) {
        if (user == null) {
            user = new User();
            user.setId(event.getId());
        }
        user.setName(event.getName());
        user.setEmail(event.getEmail());
        user.setRole(event.getRole());
        user.setStatus(event.getStatus());
        switch (event.getStatus()) {
        case ACTIVE: user.setCreatedAt(event.getTimestamp()); break;
        case SUSPENDED: user.setSuspendedAt(event.getTimestamp()); break;
        case DELETED: user.setDeletedAt(event.getTimestamp()); break;
        }
        return user;
    }
}
