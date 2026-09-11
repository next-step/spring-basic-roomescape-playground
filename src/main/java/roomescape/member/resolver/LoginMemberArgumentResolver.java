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
import roomescape.member.MemberService;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final MemberSessionStore memberSessionStore;

    public LoginMemberArgumentResolver(MemberService memberService, MemberSessionStore memberSessionStore) {
        this.memberService = memberService;
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

        Long memberId = memberSessionStore.getMemberId(session);
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        return LoginMember.from(memberService.getMember(memberId));
    }
}
