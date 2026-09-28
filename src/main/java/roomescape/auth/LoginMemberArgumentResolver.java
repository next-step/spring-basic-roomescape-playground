package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver
        implements HandlerMethodArgumentResolver {

    private final LoginService loginService;
    private final CookieTokenExtractor tokenExtractor;

    public LoginMemberArgumentResolver(LoginService loginService, CookieTokenExtractor tokenExtractor) {
        this.loginService = loginService;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request =
                webRequest.getNativeRequest(HttpServletRequest.class);

        String token = tokenExtractor.extract(request);

        return loginService.findLoginMember(token);
    }

}
