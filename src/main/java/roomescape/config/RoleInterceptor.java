package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthService;
import roomescape.member.Member;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private static final String COOKIE_NAME = "token";
    private final AuthService authService;

    public RoleInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        /**
         * 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한하세요.
         * HandlerInterceptor를 활용하여 권한이 없는 경우 401코드를 응답하세요.
         */
        Cookie[] cookies = Objects.requireNonNull(request).getCookies();
        Cookie token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new RuntimeException("권한 없음 401 에러코드"));

        Member member = authService.findMemberByToken(token.getValue());
        if (isAdmin(member)) {
            return true;
        }

        throw new RuntimeException("권한 없음 401 에러코드");
    }

    private boolean isAdmin(Member member) {
        return member.getRole().equals("ADMIN");
    }

}
