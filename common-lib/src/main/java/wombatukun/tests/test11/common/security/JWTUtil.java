package wombatukun.tests.test11.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;

public class JWTUtil {

    private final Key key;

    public JWTUtil(String signingKey) {
        this.key = Keys.hmacShaKeyFor(signingKey.getBytes());
    }

    public AuthenticatedUser validateToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims.getExpiration().before(new Date())) {
            return null;
        } else {
            return new AuthenticatedUser(
                    claims.get("user_name", String.class),
                    claims.get("authorities", List.class),
                    claims.get("user_id", Long.class)
            );
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}