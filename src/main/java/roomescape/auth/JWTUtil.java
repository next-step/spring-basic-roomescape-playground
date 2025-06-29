package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class JWTUtil {

    private final Key key;
    private final MemberDao memberDao;

    public JWTUtil(@Value("${jwt.secret}") String secret, MemberDao memberDao) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.memberDao = memberDao;
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(key)
                .compact();
    }

    public String createToken(String email, String password) {
        try {
            Member member = memberDao.findByEmailAndPassword(email, password);
            return createToken(member);
        } catch (Exception e) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
