package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.AuthorizationException;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    public LoginMemberArgumentResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null || token.isBlank()) {
            throw new AuthorizationException("인증 토큰이 존재하지 않습니다.");
        }

        Claims claims = tokenProvider.parseToken(token);
        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get("name", String.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return new LoginMember(id, name, email, role);
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
