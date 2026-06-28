package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final TokenExtractor tokenExtractor;

    public LoginMemberArgumentResolver(MemberService memberService, TokenExtractor tokenExtractor) {
        this.memberService = memberService;
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
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = tokenExtractor.extract(request);

        if (token.isBlank()) {
            return null;
        }

        return memberService.findLoginMemberByToken(token);
    }
}
