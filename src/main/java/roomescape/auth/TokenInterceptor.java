package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public TokenInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            String token = Arrays.stream(cookies)
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);

            if (token != null && !token.isBlank()) {
                LoginMember loginMember = authService.parseMemberInfo(token);
                request.setAttribute("loginMember", loginMember);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
