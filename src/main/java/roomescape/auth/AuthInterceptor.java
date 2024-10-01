package roomescape.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.member.dto.LoginMember;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        String token = cookieUtils.getToken(request);
        LoginMember loginMember = jwtProvider.getLoginMember(token);
        if (loginMember == null || !loginMember.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
