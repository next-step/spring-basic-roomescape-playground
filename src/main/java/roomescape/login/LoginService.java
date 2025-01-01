package roomescape.login;

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
        Cookie cookie = new Cookie("token", jwtTokenProvider.createToken(member.getName()));
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
            String name = jwtTokenProvider.getPayload(token);
            Member member = memberDao.findByName(name);
            return new LoginCheckResponse(member.getName());
        }
        return null;
    }
}
