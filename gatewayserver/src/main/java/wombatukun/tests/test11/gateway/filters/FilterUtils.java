package wombatukun.tests.test11.gateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public class FilterUtils {
    public static final String CORRELATION_ID = "wkd-correlation-id";

    private FilterUtils() {}

    public static String getCorrelationId(HttpHeaders requestHeaders){
        List<String> header = requestHeaders.get(CORRELATION_ID);
        if (header != null) {
            return header.isEmpty() ? null : header.get(0);
        } else {
            return null;
        }
    }

    public static ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate()
                .request(exchange.getRequest().mutate().header(name, value).build())
                .build();
    }

}
