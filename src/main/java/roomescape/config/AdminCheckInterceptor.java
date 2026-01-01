package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.util.CookieUtil;
import roomescape.util.JwtTokenProvider;

@Slf4j
@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminCheckInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = CookieUtil.extractToken(request.getCookies());

        if (token == null) {
            log.warn("관리자 페이지 접근 시도 - 로그인 필요: uri={}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
            Member member = memberService.findById(memberId);

            if (!"ADMIN".equals(member.getRole())) {
                log.warn("관리자 페이지 접근 시도 - 권한 부족: memberId={}, role={}, uri={}",
                        memberId, member.getRole(), request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            log.info("관리자 페이지 접근 성공: memberId={}, uri={}", memberId, request.getRequestURI());
            return true;
        } catch (Exception e) {
            log.error("관리자 페이지 접근 시도 - 인증 실패: uri={}, error={}", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
