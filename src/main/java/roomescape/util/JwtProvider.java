package roomescape.util;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtProvider {

    private JwtParser jwtParser;
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init(){
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();
    }




    public  String createToken(Long id, String name, String role) {

        return Jwts.builder()
                .setSubject(id.toString())
                .claim("name", name)
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String getName(String token) {
         return jwtParser
                .parseClaimsJws(token).getBody().get("name").toString();
    }

    public  String getRole(String token) {
        return jwtParser
                .parseClaimsJws(token).getBody().get("role").toString();
    }

    public Long getMemberId(String token) {
        return Long.parseLong(jwtParser
                .parseClaimsJws(token).getBody().getSubject());
    }




}
