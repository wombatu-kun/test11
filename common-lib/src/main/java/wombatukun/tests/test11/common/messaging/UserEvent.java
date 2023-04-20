package wombatukun.tests.test11.common.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.common.enums.UserStatus;
import wombatukun.tests.test11.common.security.Role;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent implements Serializable {

    private String type;
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private Date timestamp;
    private String correlationId;

}
