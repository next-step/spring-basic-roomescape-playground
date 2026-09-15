package roomescape.member.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import roomescape.member.session.MemberSessionStore;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.LoginMember;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberSessionStore memberSessionStore;

    public LoginMemberArgumentResolver(MemberSessionStore memberSessionStore) {
        this.memberSessionStore = memberSessionStore;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        LoginMember loginMember = memberSessionStore.getLoginMember(session);
        if (loginMember == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        return loginMember;
    }
}
