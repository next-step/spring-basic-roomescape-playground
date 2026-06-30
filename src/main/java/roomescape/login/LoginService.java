package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class LoginService {
    private final MemberDao memberDao;

    LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public Cookie createCookieWithToken(String email, String password){
        String token = createToken(email,password);
        return createCookie(token);
    }

    public Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public String createToken(String email, String password) {
        Member member;
        try {
            member = memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return token;
    }

    public Member getByToken(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);

        String memberId = Long.valueOf(Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody().getSubject())
                .toString();

        return memberDao.findById(memberId);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
