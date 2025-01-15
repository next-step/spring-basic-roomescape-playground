package auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.AuthorizationException;
import roomescape.domain.member.Member;

import java.util.Arrays;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtAuthManager jwtAuthManager;

    public LoginMemberArgumentResolver(JwtAuthManager jwtAuthManager) {
        this.jwtAuthManager = jwtAuthManager;
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
        jwtAuthManager.validateToken(token);

        Long id = jwtAuthManager.getId(token);
        String name = jwtAuthManager.getName(token);
        String email = jwtAuthManager.getEmail(token);
        String role = jwtAuthManager.getRole(token);

        return new Member(id, name, email, role);
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
