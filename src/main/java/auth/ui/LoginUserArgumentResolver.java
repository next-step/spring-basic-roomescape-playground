package auth.ui;

import auth.JwtUtils;
import auth.annotation.Login;
import auth.dto.LoginMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.exception.UnauthenticatedException;

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
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = jwtUtils.getTokenFromCookie(request);

        if (token == null) {
            throw new UnauthenticatedException("로그인 토큰이 존재하지 않습니다.");
        }

        try {
            Claims claims = jwtUtils.getClaims(token);
            Long id = Long.parseLong(claims.getSubject());
            String name = claims.get("name", String.class);
            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);

            return new LoginMember(id, name, email, role);
        } catch (JwtException| IllegalArgumentException e) {
            throw new UnauthenticatedException("유효하지 않은 로그인 토큰입니다.", e);
        }
    }
}
