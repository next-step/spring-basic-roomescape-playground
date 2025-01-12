package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.jwt.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

public class AdminInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;

    public AdminInterceptor(JwtProvider jwtProvider, MemberDao memberDao) {
        this.jwtProvider = jwtProvider;
        this.memberDao = memberDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = extractTokenFromCookies(cookies);
        if (token == null || token.isEmpty() || !jwtProvider.isValidToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String email = jwtProvider.extractEmail(token);
        Member member = memberDao.findByEmailAndPassword(email, null); // 비밀번호 검증은 생략


        if (member == null ||  !"ADMIN".equals(member.getRole())) { //관리자 권한
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}