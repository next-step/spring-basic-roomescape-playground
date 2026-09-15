package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private final LoginMemberResolver loginMemberResolver;

    public AdminAuthInterceptor(LoginMemberResolver loginMemberResolver) {
        this.loginMemberResolver = loginMemberResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginMember loginMember = loginMemberResolver.resolve(request);

        if (loginMember == null || !loginMember.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
