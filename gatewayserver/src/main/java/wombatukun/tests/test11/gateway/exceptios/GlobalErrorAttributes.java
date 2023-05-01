package wombatukun.tests.test11.gateway.exceptios;

import io.jsonwebtoken.JwtException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    record ExceptionRule(Class<?> exceptionClass, HttpStatus status) {}

    private final List<ExceptionRule> exceptionsRules = List.of(
            new ExceptionRule(JwtException.class, HttpStatus.UNAUTHORIZED)
    );

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);

        Optional<ExceptionRule> exceptionRuleOptional = exceptionsRules.stream()
                .map(exceptionRule -> exceptionRule.exceptionClass().isInstance(error) ? exceptionRule : null)
                .filter(Objects::nonNull)
                .findFirst();

        return exceptionRuleOptional.<Map<String, Object>>map(
                exceptionRule -> Map.of("status", exceptionRule.status().value(), "message", error.getMessage())
        ).orElseGet(
                () -> Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR,  "message", error.getMessage())
        );
    }

}
