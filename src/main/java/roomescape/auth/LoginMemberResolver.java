package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

@Component
public class LoginMemberResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginMember resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        String token = extractTokenFromCookie(cookies);
        if (token.isBlank()) {
            return null;
        }

        try {
            return jwtTokenProvider.getLoginMember(token);
        } catch (JwtException e) {
            return null;
        }
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}