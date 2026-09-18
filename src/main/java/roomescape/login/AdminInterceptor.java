package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public AdminInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        Member member = null;

        if (!token.isEmpty()) {
            try {
                member = loginService.findMemberByToken(token);
            } catch (Exception e) {
                member = null;
            }
        }

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
