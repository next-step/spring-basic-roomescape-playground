package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.UnauthorizedException;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // GET 요청(조회)은 사용자도 호출 가능
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 1) 요청에서 쿠키를 꺼내 토큰을 추출한다
        Cookie[] cookies = request.getCookies();
        String token = extractToken(cookies);

        // 2) 위 토큰으로 Member를 조회한다
        Member memberByToken = memberService.findMemberByToken(token);

        // 3-1) 로그인 상태인지 검사한다
        if (memberByToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 3-2) ADMIN이 아니면(관리자 권한 없음) 차단한다
        if (!"ADMIN".equals(memberByToken.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private  String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new UnauthorizedException("로그인이 필요합니다.");
    }


}
