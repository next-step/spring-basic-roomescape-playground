package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final String secretKey;

    public LoginService(MemberDao memberDao, @Value("${roomescape.auth.jwt.secret}") String secreteKey) {
        this.memberDao = memberDao;
        this.secretKey = secreteKey;
    }

    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginCheckResponse getUserInfoFromToken(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String memberName = claims.get("name", String.class);
        Member member = memberDao.findByName(memberName);
        return new LoginCheckResponse(member.getName());
    }
}
