package roomescape.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final String ADMIN_ROLE = "ADMIN";

    private final LoginService loginService;

    public AdminInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if (!requiresAdmin(handlerMethod)) {
            return true;
        }

        String token = CookieUtil.extractToken(request.getCookies());
        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        Member member = loginService.getByToken(token);
        if (member == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        if (!ADMIN_ROLE.equals(member.getRole())) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }

    private boolean requiresAdmin(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
    }
}
