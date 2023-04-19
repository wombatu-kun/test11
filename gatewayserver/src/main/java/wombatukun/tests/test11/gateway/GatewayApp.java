package wombatukun.tests.test11.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;
import wombatukun.tests.test11.common.security.JWTUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
public class GatewayApp {

	@Value("${auth.signing-key}")
	private String signingKey;

	@Bean
	public JWTUtil jwtUtil() {
		return new JWTUtil(signingKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}

	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
				.map(InetSocketAddress::getAddress)
				.map(InetAddress::getHostAddress)
				.map(Mono::just)
				.orElse(Mono.empty());
	}

}
