package wombatukun.tests.test11.shippingservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import wombatukun.tests.test11.shippingservice.enums.Role;


import java.util.Objects;

@Data
@AllArgsConstructor
public class AuthInfo {
    private final Long id;
    private final Role role;

    public static AuthInfo fromAuthentication(Authentication authentication) {
        return new AuthInfo(
                (Long) authentication.getDetails(),
                Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthInfo authInfo = (AuthInfo) o;
        return id.equals(authInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
