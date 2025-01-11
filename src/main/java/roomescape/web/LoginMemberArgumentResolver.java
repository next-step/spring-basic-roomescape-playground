package roomescape.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.application.AuthService;
import roomescape.application.AuthorizationException;
import roomescape.member.Member;


import java.util.Arrays;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = extractTokenFromCookies(request.getCookies());

        if (!authService.verifyToken(token)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }

        String email = authService.getEmailFromToken(token);

        return authService.findLoginMemberByEmail(email);
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("토큰 쿠키가 없습니다."));
    }
}
