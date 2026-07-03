package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.AuthenticationException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final AuthCookieProvider authCookieProvider;

    public LoginMemberArgumentResolver(MemberService memberService, AuthCookieProvider authCookieProvider) {
        this.memberService = memberService;
        this.authCookieProvider = authCookieProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && LoginMemberInfo.class.isAssignableFrom(parameter.getParameterType());
    }


    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        LoginMember loginMember = parameter.getParameterAnnotation(LoginMember.class);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return handleAuthenticationFailure(loginMember);
        }

        try {
            String token = authCookieProvider.extractToken(request);
            return memberService.checkLogin(token);
        } catch (RuntimeException e) {
            return handleAuthenticationFailure(loginMember);
        }
    }

    private Object handleAuthenticationFailure(LoginMember loginMember) {
        if (loginMember != null && !loginMember.required()) {
            return null;
        }
        throw new AuthenticationException();
    }
}
