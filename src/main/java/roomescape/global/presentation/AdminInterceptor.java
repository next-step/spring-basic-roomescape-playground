package roomescape.global.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.auth.AuthService;
import roomescape.global.token.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.dao.MemberDao;
import roomescape.member.entity.Role;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;
    private final AuthService authService;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider, MemberDao memberDao, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Member member = authService.extractMember(request);

        if (member == null || !(Role.ADMIN).equals(member.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
