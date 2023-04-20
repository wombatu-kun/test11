package wombatukun.tests.test11.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.common.enums.UserStatus;
import wombatukun.tests.test11.common.security.Role;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private UserStatus status;
    private Date createdAt;

}
