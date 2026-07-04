package roomescape.login;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.token.TokenProvider;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    public LoginMemberArgumentResolver() {}

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        String token = extractTokenFromCookie(cookies);

        if (token.isBlank()) {
            throw new UnauthorizedException();
        }

        try {
            Claims claims = TokenProvider.extractClaims(token);

            Long id = Long.valueOf(claims.getSubject());
            String name = claims.get("name", String.class);
            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);

            return new LoginMember(id, name, email, role);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
