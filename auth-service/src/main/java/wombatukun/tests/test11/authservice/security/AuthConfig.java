package wombatukun.tests.test11.authservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private String client;
    private String secret;
    private String signingKey;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;

}
