package wombatukun.tests.test11.adminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@RefreshScope
@EnableAdminServer
@EnableDiscoveryClient
@EnableWebFluxSecurity
@SpringBootApplication
public class AdminApp {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AdminApp.class)
				.web(WebApplicationType.REACTIVE)
				.run(args);
	}

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
		return http
				.csrf()
				.disable()
				.build();
	}

}
