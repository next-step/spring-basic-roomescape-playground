package roomescape.golbal;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.provider.CookieProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.provider.TokenProvider;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private CookieProvider cookieProvider;

    public AuthHandlerInterceptor(MemberDao memberDao, TokenProvider tokenProvider, CookieProvider cookieProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = cookieProvider.extractTokenFromCookie(cookies);
        String name = tokenProvider.getMemberNameFromToken(token);
        Member member = memberDao.findByName(name);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

}
