package roomescape.auth;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.LoginMember;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        if (!request.getRequestURI().startsWith("/admin")) {
            return true;
        }
        if (request.getCookies() == null) {
            response.setStatus(401);
            return false;
        }
        String token = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("token"))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .getValue();
        LoginMember loginMember = jwtProvider.getLoginMember(token);
        System.out.println(loginMember);
        if (loginMember == null || !loginMember.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
