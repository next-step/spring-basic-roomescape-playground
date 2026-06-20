package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {

    private final String secretKey;

    public JwtUtils(String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(Long memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getMemberId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}