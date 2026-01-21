package roomescape.config;

import roomescape.auth.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.util.CookieUtil;

@Slf4j
@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;

    public AdminCheckInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
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
            Long memberId = jwtUtils.getId(token);
            String role = jwtUtils.getRole(token);

            if (!"ADMIN".equals(role)) {
                log.warn("관리자 페이지 접근 시도 - 권한 부족: memberId={}, role={}, uri={}",
                        memberId, role, request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("관리자 페이지 접근 시도 - 인증 실패: uri={}, error={}", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
