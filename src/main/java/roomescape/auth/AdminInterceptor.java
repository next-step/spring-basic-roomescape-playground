package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberService;

public class AdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(
            MemberService memberService,
            TokenExtractor tokenExtractor,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberService = memberService;
        this.tokenExtractor = tokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = tokenExtractor.extract(request);

        if (token.isBlank()) {
            throw new UnauthorizedException();
        }

        try {
            Long memberId = jwtTokenProvider.extractMemberId(token);
            LoginMember loginMember = memberService.findLoginMemberById(memberId);

            if (!"ADMIN".equals(loginMember.getRole())) {
                throw new UnauthorizedException();
            }

            return true;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }
}
