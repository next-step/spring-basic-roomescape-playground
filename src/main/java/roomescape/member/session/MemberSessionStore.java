package roomescape.member.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import roomescape.member.LoginMember;

@Component
public class MemberSessionStore {
    private static final String LOGIN_MEMBER = "loginMember";

    public void saveLoginMember(HttpSession session, LoginMember loginMember) {
        session.setAttribute(LOGIN_MEMBER, loginMember);
    }

    public LoginMember getLoginMember(HttpSession session) {
        return (LoginMember) session.getAttribute(LOGIN_MEMBER);
    }
}
