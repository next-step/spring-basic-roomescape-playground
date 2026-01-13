package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.UnauthorizedException;
import roomescape.model.Member;
import roomescape.service.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class) && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = JwtTokenProvider.extractTokenFromCookies(request.getCookies());
        LoginMember loginMember = parameter.getParameterAnnotation(LoginMember.class);

        if (token == null) {
            if (loginMember.required()) throw new UnauthorizedException("로그인이 필요합니다.");

            return null;
        }

        String subject = jwtTokenProvider.getClaims(token).getSubject();

        // TODO: 불필요한 DB 접근 로직 제거
        return memberService.findById(subject);
    }
}
