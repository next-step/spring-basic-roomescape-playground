package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;
    private JwtTokenUtil jwtTokenUtil;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenUtil jwtTokenUtil) {
        this.memberService = memberService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        Long memberId = jwtTokenUtil.getPayload(httpServletRequest);
        Member member = memberService.findMemberById(memberId);

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
