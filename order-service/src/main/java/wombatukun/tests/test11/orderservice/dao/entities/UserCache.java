package wombatukun.tests.test11.orderservice.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import wombatukun.tests.test11.common.enums.UserStatus;
import wombatukun.tests.test11.common.security.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("user")
public class UserCache {

    @Id
    private Long id;
    private Role role;
    private UserStatus status;

}
