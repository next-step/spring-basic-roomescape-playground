package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.util.CookieUtil;
import roomescape.util.JwtUtil;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieUtil.extractToken(request.getCookies());

        if (token == null) {
            log.warn("로그인이 필요한 요청: uri={}", request.getRequestURI());
            throw new NotFoundDataException("로그인이 필요합니다.");
        }

        try {
            Long memberId = JwtUtil.getMemberIdFromToken(token);
            Member member = memberService.findById(memberId);

            log.debug("로그인 사용자 인증 성공: memberId={}, uri={}", memberId, request.getRequestURI());
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (Exception e) {
            log.error("토큰 인증 실패: uri={}, error={}", request.getRequestURI(), e.getMessage());
            throw new NotFoundDataException("유효하지 않은 인증 정보입니다.");
        }
    }
}
