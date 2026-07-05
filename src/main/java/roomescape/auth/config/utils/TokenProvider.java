package roomescape.auth.config.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
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
    private final Key signingKey;
    private final JwtParser jwtParser;

    public TokenProvider(@Value("${jwt.secret}") String secretKeyString) {
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);

        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }

    public String createToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰이에요. 다시 로그인해 주세요!");
        }
    }

    //jwt중에 페이로드에서 email뽑아서 반환
    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
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
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}