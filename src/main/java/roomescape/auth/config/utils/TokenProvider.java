package roomescape.auth.config.utils;

import io.jsonwebtoken.JwtException;
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
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰이에요. 다시 로그인해 주세요!");
        }
    }

    //jwt중에 페이로드에서 email뽑아서 반환
    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();  //subject로 이메일 넣어뒀었음

        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("토큰에서 정보를 꺼내는데 문제가 생겼어요!");
        }
    }

    public String getRoleFromPayload(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}