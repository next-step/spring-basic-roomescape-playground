package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.Login.LoginService;
import roomescape.member.Member;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private LoginService loginService;

    public AuthInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue).orElseThrow(() -> new IllegalArgumentException("토큰이 없습니다."));

        if (token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        Member member = loginService.findByName(loginService.checkLogin(token).getName());

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}