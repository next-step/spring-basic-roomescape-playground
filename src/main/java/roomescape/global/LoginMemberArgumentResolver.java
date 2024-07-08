package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import auth.JwtUtils;
import roomescape.global.constant.AuthConstant;
import roomescape.domain.member.dto.LoginMember;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    public LoginMemberArgumentResolver(final JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        // 컨트롤러 메서드의 인자가 LoginMember 타입일 때, 해당 리졸버가 사용
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory)
            throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = CookieUtil.getTokenFromCookie(request, AuthConstant.TOKEN);
        final String parsedName = jwtUtils.getValueFromJwtToken(token, AuthConstant.NAME);
        final String parsedRole = jwtUtils.getValueFromJwtToken(token, AuthConstant.ROLE);
        final Long parsedId = jwtUtils.getTokenPrincipal(token);

        return new LoginMember(parsedId, parsedName, parsedRole);
    }
}
