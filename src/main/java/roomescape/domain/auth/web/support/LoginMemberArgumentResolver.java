package roomescape.domain.auth.web.support;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.global.exception.UnauthorizedException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private SessionManager sessionManager;

    public LoginMemberArgumentResolver(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class) && parameter.getParameterType().equals(LoginMember.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Login annotation = parameter.getParameterAnnotation(Login.class);
        LoginMember loginMember = sessionManager.extractOrNull(request);

        if (annotation.required() && loginMember == null) {
            throw new UnauthorizedException();
        }

        return loginMember;
    }
}
