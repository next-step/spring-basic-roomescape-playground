package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminApiInterceptor implements HandlerInterceptor {

    private final AdminAuthInterceptor adminAuthInterceptor;

    public AdminApiInterceptor(AdminAuthInterceptor adminAuthInterceptor) {
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("GET")) {
            return true;
        }

        return adminAuthInterceptor.preHandle(request, response, handler);
    }
}
