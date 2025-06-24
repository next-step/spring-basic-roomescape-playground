package roomescape;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.JWTUtil;
import roomescape.member.LoginMember;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtil jwtUtil;

    public LoginMemberArgumentResolver(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                Claims claims = jwtUtil.parseToken(token);
                return new LoginMember(
                        Long.valueOf(claims.getSubject()),
                        claims.get("name", String.class),
                        null,
                        claims.get("role", String.class)
                );
            }
        }

        return null;
    }
}
