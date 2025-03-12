package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.CookieExtractor;
import roomescape.auth.AuthService;
import roomescape.member.Member;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final CookieExtractor cookieExtractor;

    public RoleInterceptor(AuthService authService, CookieExtractor cookieExtractor) {
        this.authService = authService;
        this.cookieExtractor = cookieExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie cookie = cookieExtractor.extractToken(request);

        Member member = authService.findMemberByToken(cookie.getValue());

        if (isAdmin(member)) {
            return true;
        }

        throw new RuntimeException("권한 없음 401 에러코드");
    }

    private boolean isAdmin(Member member) {
        return member.getRole().equals("ADMIN");
    }

}
