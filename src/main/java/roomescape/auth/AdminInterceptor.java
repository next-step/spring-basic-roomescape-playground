package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final CookieTokenExtractor tokenExtractor;

    public AdminInterceptor(LoginService loginService, CookieTokenExtractor tokenExtractor) {
        this.loginService = loginService;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (!(handler instanceof HandlerMethod handlerMethod)
                || !handlerMethod.hasMethodAnnotation(AdminOnly.class)) {
            return true;
        }

        String token = tokenExtractor.extract(request);
        LoginMember member = loginService.findLoginMember(token);

        if (!member.isAdmin()) {
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }

        return true;
    }

}
