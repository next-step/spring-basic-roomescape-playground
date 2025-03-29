package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.dto.MemberResponse;

@Component
public class JwtProvider {

    private static final String HEADER_TYPE = "typ";
    private static final String TOKEN_TYPE = "JWT";

    private final String secretKey;

    public JwtProvider(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(MemberResponse memberResponse) {
        Map<String, String> claims = new HashMap<>();
        claims.put("name", memberResponse.name());
        claims.put("email", memberResponse.email());
        claims.put("role", memberResponse.role());
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .setHeaderParam(HEADER_TYPE, TOKEN_TYPE)
                .setSubject(memberResponse.id().toString())
                .claim("name", memberResponse.name())
                .claim("email", memberResponse.email())
                .claim("role", memberResponse.role())
                .compact();
    }

    public LoginMember parseLoginMemberFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return getLoginMember(claims);
    }

    private Claims getClaimFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (IllegalArgumentException | UnsupportedJwtException | MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 토큰 형식 입니다.", e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료 되었습니다.", e);
        } catch (SignatureException e) {
            throw new SecurityException("토큰의 서명이 위조 되었습니다.", e);
        }
    }

    private LoginMember getLoginMember(Claims claims) {
        return new LoginMember(
                Long.valueOf(claims.getSubject()),
                claims.get("name", String.class),
                claims.get("email", String.class),
                claims.get("role", String.class)
        );
    }

}
