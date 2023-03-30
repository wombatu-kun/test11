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
import static wombatukun.tests.test11.gateway.filters.FilterUtils.setRequestHeader;

@Slf4j
@Component
public class PreFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (getCorrelationId(requestHeaders) != null) {
            log.debug("{} found in tracking filter: {}", CORRELATION_ID, getCorrelationId(requestHeaders));
        } else {
            String correlationID = java.util.UUID.randomUUID().toString();
            exchange = setRequestHeader(exchange, CORRELATION_ID, correlationID);
            log.debug("{} generated in tracking filter: {}", CORRELATION_ID, correlationID);
        }
        return chain.filter(exchange);
    }

}
