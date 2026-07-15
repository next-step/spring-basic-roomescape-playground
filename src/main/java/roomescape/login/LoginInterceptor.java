package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.CookieManager;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;
    private final CookieManager cookieManager;

    public LoginInterceptor(JwtProvider jwtProvider, MemberDao memberDao, CookieManager cookieManager) {
        this.jwtProvider = jwtProvider;
        this.memberDao = memberDao;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        String token = cookieManager.extractToken(cookies, "accessToken");
        Long memberId = jwtProvider.getMemberId(token);
        Member member = memberDao.findById(memberId);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
