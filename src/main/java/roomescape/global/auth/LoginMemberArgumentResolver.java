package roomescape.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.entity.LoginMember;
import roomescape.domain.member.service.MemberService;
import roomescape.global.auth.jwt.CookieTokenExtractor;
import roomescape.global.auth.jwt.JwtTokenProvider;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = CookieTokenExtractor.extract(request);
        Long memberId = jwtTokenProvider.getSubject(token);
        return memberService.readAuthorizedMemberById(memberId);
    }
}
