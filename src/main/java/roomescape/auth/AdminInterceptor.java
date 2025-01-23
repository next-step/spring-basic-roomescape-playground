package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie tokenCookie = getCookie(request, "token");
        Map<String, Object> claims = jwtTokenProvider.getClaims(tokenCookie.getValue());
        LoginMember member = LoginMember.fromClaims(claims);

        if (member == null || !member.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    public Cookie getCookie(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Empty cookie"));
    }
}
