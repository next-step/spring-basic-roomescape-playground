package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.MemberDao;
import roomescape.member.Member;

@Component
public class JwtProvider {

    private final String secretKey;
    private final MemberDao memberDao;

    public JwtProvider(MemberDao memberDao, @Value("${JWT-Key}")String secretKey) {
        this.memberDao = memberDao;
        this.secretKey = secretKey;
    }

    public String createToken(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null)
            throw new IllegalArgumentException("Invalid email or password");

        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public String getNameFromToken(String token) {
        return (String)Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().get("name");
    }

    public Long getIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject());
    }
}
