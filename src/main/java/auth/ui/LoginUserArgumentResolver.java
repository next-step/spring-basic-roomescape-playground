package auth.ui;

import auth.JwtUtils;
import auth.annotation.Login;
import auth.dto.LoginMember;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Optional;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    public LoginUserArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class)
                && parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Optional<Cookie> tokenCookie = findTokenCookie(request.getCookies());
        if (tokenCookie.isEmpty()) {
            return null;
        }
        String token = tokenCookie.get().getValue();

        Claims claims = jwtUtils.getClaims(token);
        Long id = Long.parseLong(claims.getSubject());
        String name = claims.get("name", String.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return new LoginMember(id, name, email, role);
    }

    private Optional<Cookie> findTokenCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst();
    }
}
