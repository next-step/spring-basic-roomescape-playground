package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    public static final String ATTRIBUTE_AUTHORIZED_MEMBER_KEY = "r.a.AuthService#authorizedMember";

    private final AuthTokenProvider authTokenProvider;

    public AuthenticationInterceptor(AuthTokenProvider authTokenProvider) {
        this.authTokenProvider = authTokenProvider;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return true;
        }

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("token")) {
                continue;
            }

            AuthToken token = new AuthToken(cookie.getValue());
            AuthorizedMember authorizedMember = authTokenProvider.parseSessionToken(token);

            request.setAttribute(ATTRIBUTE_AUTHORIZED_MEMBER_KEY, authorizedMember);
            return true;
        }

        return true;
    }
}
