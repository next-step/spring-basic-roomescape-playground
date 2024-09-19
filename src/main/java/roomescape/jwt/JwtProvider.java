package roomescape.jwt;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class JwtProvider {
    private String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private MemberDao memberDao;

    public JwtProvider(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String createToken(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

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
}
