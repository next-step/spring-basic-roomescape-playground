package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.security.auth.message.config.AuthConfigProvider;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import org.springframework.beans.factory.annotation.Value;
public class JwtUtils  {
    private final String secretKey;

    public JwtUtils(String secretKey) {
        this.secretKey = secretKey;
    }
    public String createToken(Member member) {

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        return accessToken;

    }
}
