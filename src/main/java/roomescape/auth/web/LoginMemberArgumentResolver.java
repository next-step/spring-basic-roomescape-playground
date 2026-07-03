package roomescape.auth.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.service.AuthService;
import roomescape.exception.ApplicationException;
import roomescape.util.CookieUtil;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    public LoginMemberArgumentResolver(AuthService authService, CookieUtil cookieUtil) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMember = parameter.getParameterType().equals(LoginMember.class);
        boolean hasLoginAnno = parameter.hasParameterAnnotation(Login.class);

        return isLoginMember && hasLoginAnno;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS);
        }

        String token = cookieUtil.extractToken(cookies)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS));

        return authService.findAuthenticatedMember(token);
    }
}
