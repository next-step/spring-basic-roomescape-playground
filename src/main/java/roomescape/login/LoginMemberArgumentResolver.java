package roomescape.login;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final LoginService loginService;

    public LoginMemberArgumentResolver(MemberService memberService, LoginService loginService) {
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginMemberAnnotation && isMemberType;
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                       NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Long memberId = loginService.getMemberId(request.getCookies())
            .orElseThrow(() -> new IllegalStateException("로그인 정보가 없습니다."));

        MemberResponse memberResponse = memberService.findById(memberId);
        return new Member(memberResponse.getId(), memberResponse.getName(), memberResponse.getEmail(), memberResponse.getRole());
    }
}
