package roomescape.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.AuthenticationMember;
import roomescape.member.MemberService;
import roomescape.util.CookieUtil;
import roomescape.util.JwtUtil;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;
    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;

    public LoginMemberArgumentResolver(MemberService memberService, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.memberService = memberService;
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthenticationMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = cookieUtil.extractTokenFromCookie(request);
        Long memberId = jwtUtil.getMemberId(token);
        String name = jwtUtil.getName(token);
        String role = jwtUtil.getRole(token);

        return new AuthenticationMember(memberId, name, role);
    }
}
