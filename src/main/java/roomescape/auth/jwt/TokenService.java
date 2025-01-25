package roomescape.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    private static final String NAME_CLAIM = "name";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    private final JwtConfig jwtConfig;
    private Key key;

    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String createToken(MemberTokenDto memberTokenDto) {
        return Jwts.builder()
                .setSubject(memberTokenDto.id().toString())
                .claim(NAME_CLAIM, memberTokenDto.name())
                .claim(EMAIL_CLAIM, memberTokenDto.email())
                .claim(ROLE_CLAIM, memberTokenDto.role())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key)
                .compact();
    }

    public boolean checkValidToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public MemberTokenDto getMemberClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new MemberTokenDto(
                    Long.valueOf(claims.getSubject()),
                    claims.get(NAME_CLAIM).toString(),
                    claims.get(EMAIL_CLAIM).toString(),
                    claims.get(ROLE_CLAIM).toString());
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
    }
}
