package wombatukun.tests.test11.gateway.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import wombatukun.tests.test11.common.security.JWTUtil;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        return Mono.just(jwtUtil.validateToken(authToken))
                .filter(Objects::nonNull)
                .map(validUser -> {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                    validUser.getUserName(),
                                    null,
                                    validUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                            );
                    auth.setDetails(validUser.getUserId());
                    return auth;
                });
    }

}
