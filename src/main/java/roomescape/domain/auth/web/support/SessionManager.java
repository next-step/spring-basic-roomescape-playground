package roomescape.domain.auth.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.auth.principal.LoginMember;

@Component
public class SessionManager {

    private static final String LOGIN_MEMBER_SESSION_KEY = "LOGIN_MEMBER";

    private final String sessionCookieName;

    public SessionManager(
            @Value("${server.servlet.session.cookie.name:JSESSIONID}") String sessionCookieName
    ) {
        this.sessionCookieName = sessionCookieName;
    }

    public void store(HttpServletRequest request, LoginMember loginMember) {
        clear(request);
        request.getSession(true).setAttribute(LOGIN_MEMBER_SESSION_KEY, loginMember);
    }

    public LoginMember extractOrNull(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return (LoginMember) session.getAttribute(LOGIN_MEMBER_SESSION_KEY);
    }

    public void clear(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
