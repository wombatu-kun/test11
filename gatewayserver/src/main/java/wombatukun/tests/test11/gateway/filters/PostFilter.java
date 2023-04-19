package wombatukun.tests.test11.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static wombatukun.tests.test11.gateway.filters.FilterUtils.CORRELATION_ID;
import static wombatukun.tests.test11.gateway.filters.FilterUtils.getCorrelationId;

@Slf4j
@Component
public class PostFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            String correlationId = getCorrelationId(requestHeaders);
            log.debug("Adding the {}={} to the outbound headers", CORRELATION_ID, correlationId);
            exchange.getResponse().getHeaders().add(CORRELATION_ID, correlationId);
        }));
    }

}
