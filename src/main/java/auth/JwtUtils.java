package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${roomescape.auth.jwt.secret}")
    private String secret;

    @Bean
    public String createToken(Long id, String name, String role) {
        String secretKey = secret;
        String accessToken = Jwts.builder()
                .setSubject(id.toString())
                .claim("name", name)
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return accessToken;
    }

    @Bean
    public Long extractIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

}
