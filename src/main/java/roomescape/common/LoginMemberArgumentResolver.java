package roomescape.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.AuthenticationMember;
import roomescape.util.CookieUtil;
import roomescape.util.JwtProvider;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthenticationMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieUtil.extractTokenFromCookie(request);
        Long memberId = jwtProvider.getMemberId(token);
        String name = jwtProvider.getName(token);
        String role = jwtProvider.getRole(token);

        return new AuthenticationMember(memberId, name, role);
    }
}
