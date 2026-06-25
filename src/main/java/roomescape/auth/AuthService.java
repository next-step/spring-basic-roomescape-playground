package roomescape.auth;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthTokenProvider authTokenProvider;

    public AuthService(AuthTokenProvider authTokenProvider) {
        this.authTokenProvider = authTokenProvider;
    }

    public @Nullable AuthorizedMember tryAuthenticateRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("token")) {
                continue;
            }

            AuthToken token = new AuthToken(cookie.getValue());
            return authTokenProvider.parseSessionToken(token);
        }

        return null;
    }
}
