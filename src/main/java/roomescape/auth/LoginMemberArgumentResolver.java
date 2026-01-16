package roomescape.auth;

import auth.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.LoginMemberDto;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    public LoginMemberArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMemberDto.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwtUtils.extractTokenFromCookies(request.getCookies());

        if (token.isEmpty()) {
            return null;
        }

        try {
            Claims claims = jwtUtils.parseClaims(token);

            Long id = Long.valueOf(claims.getSubject());
            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);

            return new LoginMemberDto(id, name, null, role);
        } catch (ExpiredJwtException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}