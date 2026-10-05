package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!isAdminRequest(request)) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        if (token.isBlank()) {
            response.setStatus(401);
            return false;
        }

        Long memberId = Long.valueOf(
                Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );

        Member member = memberService.findById(memberId);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    private boolean isAdminRequest(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (uri.equals("/admin") || uri.startsWith("/admin/")) {
            return true;
        }

        if (uri.startsWith("/themes") || uri.startsWith("/times")) {
            return method.equals("POST") || method.equals("DELETE");
        }

        return false;
    }
}
