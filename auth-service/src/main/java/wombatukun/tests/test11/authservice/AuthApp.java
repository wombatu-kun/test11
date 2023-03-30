package wombatukun.tests.test11.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableResourceServer
@EnableAuthorizationServer
public class AuthApp {

	public static void main(String[] args) {
		SpringApplication.run(AuthApp.class, args);
	}

}
