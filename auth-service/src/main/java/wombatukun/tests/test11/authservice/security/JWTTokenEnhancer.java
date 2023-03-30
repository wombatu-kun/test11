package wombatukun.tests.test11.authservice.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.authservice.dao.entities.User;
import wombatukun.tests.test11.authservice.dao.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTTokenEnhancer implements TokenEnhancer {

    private final UserRepository userRepository;

    private Long getUserId(String email){
        User user = userRepository.findByEmail(email);
        return user.getId();
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        Long userId = getUserId(authentication.getName());
        additionalInfo.put("user_id", userId);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
