package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(Long memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getMemberId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}