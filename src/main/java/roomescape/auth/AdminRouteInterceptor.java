package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.model.Member;
import roomescape.service.MemberService;

@Component
public class AdminRouteInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AdminRouteInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        if (!method.hasMethodAnnotation(AdminRoute.class) && !method.getBeanType()
                .isAnnotationPresent(AdminRoute.class)) {
            return true;
        }

        String token = JwtTokenProvider.extractTokenFromCookies(request.getCookies());

        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String subject = jwtTokenProvider.getSubject(token);
        Member member = memberService.findById(subject);

        if (member == null || member.getRole() != Role.ADMIN) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}
