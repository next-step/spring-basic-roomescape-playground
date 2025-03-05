package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import roomescape.login.jwt.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class LoginService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member foundMember = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        String accessToken = jwtTokenProvider.createToken(foundMember);
        return new LoginResponse(accessToken);
    }

    public MemberResponse checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        Long memberId = jwtTokenProvider.getMemberId(token);
        Member member = memberDao.findById(memberId);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}
