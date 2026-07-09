package roomescape.auth.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.service.AuthService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final TokenExtractor tokenExtractor;

    public LoginMemberArgumentResolver(AuthService authService, TokenExtractor tokenExtractor) {
        this.authService = authService;
        this.tokenExtractor = tokenExtractor;
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

        String token = tokenExtractor.extractAccessToken(
                request.getCookies()
        );

        return authService.findAuthenticatedMember(token);
    }
}
