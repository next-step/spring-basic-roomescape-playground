package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final CookieManager cookieExtractor;

    public LoginMemberArgumentResolver(MemberService memberService, JwtProvider jwtProvider, CookieManager cookieExtractor) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
        this.cookieExtractor = cookieExtractor;
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

        String token = cookieExtractor.extractTokenFromCookie(request.getCookies());

        if (token.isBlank()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Long memberId = jwtProvider.extractMemberId(token);

        Member member = memberService.findById(memberId);

        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
