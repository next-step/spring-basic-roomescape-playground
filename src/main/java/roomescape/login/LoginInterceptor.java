package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.Arrays;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;

    public LoginInterceptor(JwtProvider jwtProvider, MemberDao memberDao) {
        this.jwtProvider = jwtProvider;
        this.memberDao = memberDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = getCookies(request);

        String token = extractToken(cookies);
        Long memberId = jwtProvider.getMemberId(token);
        Member member = memberDao.findById(memberId);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private Cookie[] getCookies(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new IllegalStateException("cookie is not exist");
        }
        return cookies;
    }

    private String extractToken(Cookie[] cookies) {
        String token = Arrays.stream(cookies)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("token is not found in cookies"));
        return token;
    }
}
