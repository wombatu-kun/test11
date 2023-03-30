package wombatukun.tests.test11.shippingservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUser {
    String userName;
    List<String> roles;
    Long userId;
}
