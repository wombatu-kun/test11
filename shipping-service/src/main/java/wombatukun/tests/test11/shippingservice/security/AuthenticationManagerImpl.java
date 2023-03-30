package wombatukun.tests.test11.shippingservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final JWTUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        AuthenticatedUser authUser = jwtUtil.validateToken(token);
        if (authUser != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    authUser.userName,
                    null,
                    authUser.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            auth.setDetails(authUser.getUserId());
            return auth;
        } else {
            return null;
        }
    }

}
