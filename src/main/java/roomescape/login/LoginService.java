package roomescape.login;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class LoginService {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Cookie login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        Cookie cookie = new Cookie("token", jwtTokenProvider.createToken(member.getEmail(), member.getPassword()));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public LoginCheckResponse loginCheck(Cookie[] cookies) {
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }

        if (token != null) {
            Claims claims = jwtTokenProvider.getPayload(token);
            System.out.println("claims = " + claims);
            String email = String.valueOf(claims.get("email"));
            String password = String.valueOf(claims.get("password"));
            Member member = memberDao.findByEmailAndPassword(email, password);
            return new LoginCheckResponse(member.getName());
        }
        return null;
    }
}

