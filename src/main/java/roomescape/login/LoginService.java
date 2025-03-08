package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    private MemberDao memberDao;

    @Autowired
    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Map<String, String> getUserInfoFromToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());

        Member member = memberDao.findByName(memberId.toString());

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("name", member.getName());
        return userInfo;
    }
}
