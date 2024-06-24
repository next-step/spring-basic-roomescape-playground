package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import roomescape.member.MemberResponse;

@Component
public class JwtTokenProvider {

    public String createToken( MemberResponse member) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return accessToken;
    }
}