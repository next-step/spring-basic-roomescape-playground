package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.LoginSession;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private MemberService memberService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final String INVALID_MEMBERID = "회원 아이디를 찾을 수 없습니다.";
    private final String INVALID_MEMBER = "유효하지 않은 회원 정보입니다.";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginSession.class)
                && parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        Long memberId = jwtTokenUtil.getPayload(httpServletRequest.getCookies());
        if(memberId == null) {
            throw new IllegalArgumentException(INVALID_MEMBERID);
        }

        Member member = memberService.findMemberById(memberId);
        if(member == null) {
            throw new IllegalArgumentException(INVALID_MEMBER);
        }

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
