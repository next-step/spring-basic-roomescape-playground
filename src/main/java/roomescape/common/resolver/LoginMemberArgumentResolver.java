package roomescape.common.resolver;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.constants.AuthConstants;
import roomescape.auth.util.CookieManager;
import roomescape.auth.service.AuthService;
import roomescape.auth.dto.LoginMember;
import roomescape.member.dto.Member;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        CookieManager cookieManager = new CookieManager(request.getCookies());
        String accessToken = cookieManager.getValue(AuthConstants.AUTH_TOKEN_COOKIE);

        Member member = authService.getLoginMember(accessToken);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
