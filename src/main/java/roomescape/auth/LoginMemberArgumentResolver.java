package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.LoginMember;
import roomescape.util.JwtUtil;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final String secretKey;

    public LoginMemberArgumentResolver(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = JwtUtil.extractTokenFromCookies(request.getCookies());

        if (token.isEmpty()) {
            return null;
        }

        Claims claims = JwtUtil.parseClaims(token, secretKey);

        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        return new LoginMember(id, name, null, role);
    }
}