package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRole;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "roomescape-secret-key-for-jwt-token";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(Member member) {
        return createAccessToken(member);
    }

    public String createAccessToken(Member member) {
        return createToken(member, TokenType.ACCESS, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String createRefreshToken(Member member) {
        return createToken(member, TokenType.REFRESH, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private String createToken(Member member, TokenType tokenType, long expirationTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(member.email())
                .claim("id", member.id())
                .claim("name", member.name())
                .claim("role", member.role().name())
                .claim("type", tokenType.name())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public LoginMemberInfo parseMember(String token) {
        return parseAccessToken(token);
    }

    public LoginMemberInfo parseAccessToken(String token) {
        return parseMember(token, TokenType.ACCESS);
    }

    public LoginMemberInfo parseRefreshToken(String token) {
        return parseMember(token, TokenType.REFRESH);
    }

    private LoginMemberInfo parseMember(String token, TokenType expectedTokenType) {
        try {
            Claims claims = parseClaims(token);
            validateTokenType(claims, expectedTokenType);
            Long id = claims.get("id", Number.class).longValue();
            String name = claims.get("name").toString();
            String email = claims.getSubject();
            MemberRole role = MemberRole.from(claims.get("role").toString());
            return new LoginMemberInfo(id, name, email, role);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private void validateTokenType(Claims claims, TokenType expectedTokenType) {
        TokenType tokenType = TokenType.from(claims.get("type").toString());
        if (tokenType != expectedTokenType) {
            throw new IllegalArgumentException("Invalid token type");
        }
    }

    private Claims parseClaims(String token) {
        if (!isValidSignature(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        return jwtParser()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isValidSignature(String token) {
        try {
            jwtParser().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build();
    }
}
