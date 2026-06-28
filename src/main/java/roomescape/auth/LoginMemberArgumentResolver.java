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
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(
            MemberService memberService,
            TokenExtractor tokenExtractor,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberService = memberService;
        this.tokenExtractor = tokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
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

        Long memberId = jwtTokenProvider.extractMemberId(token);
        return memberService.findLoginMemberById(memberId);
    }
}
