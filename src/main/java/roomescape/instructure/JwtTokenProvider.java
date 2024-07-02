package roomescape.instructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import roomescape.member.MemberResponse;

@Component
public class JwtTokenProvider {
    @Value("${roomescape.auth.jwt.secret}")
    String secretKey;

    public String createToken( MemberResponse member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return accessToken;
    }
}