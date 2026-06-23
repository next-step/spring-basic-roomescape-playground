package roomescape.auth.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class TokenProvider {

    private static final long EXPIRATION_TIME = 3600000; // 1시간

    @Value("${jwt.secret}")
    private String SECRET_KEY_STRING;

    // SHA만쓰면 해커가 위조해도 서버가 모름. HMAC은 서버에 문자열 두고 그걸 합쳐서 생성해보는거라 해커가 토큰 바꾼거 잡아낼 수 있음
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .serializeToJsonWith(new io.jsonwebtoken.gson.io.GsonSerializer<>())
                .compact();
    }
}