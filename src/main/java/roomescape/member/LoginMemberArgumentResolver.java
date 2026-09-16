package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.TokenUtil;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final TokenUtil tokenUtil;

    public LoginMemberArgumentResolver(MemberService memberService, TokenUtil tokenUtil) {
        this.memberService = memberService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = tokenUtil.extractToken(request);
        if (token.isEmpty()) {
            throw new AuthorizationException("쿠키에 토큰이 없습니다.");
        }

        Member member = memberService.findMemberByToken(token);
        return LoginMember.from(member);
    }
}
