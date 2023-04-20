package wombatukun.tests.test11.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.authservice.enums.Status;
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
    private Status status;
    private Date createdAt;

}
