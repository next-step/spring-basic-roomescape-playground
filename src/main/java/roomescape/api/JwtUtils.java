package roomescape.api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;

public class JwtUtils {

    private String secretKey;

    public JwtUtils() {

    }

    public JwtUtils(String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(Member member){
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
