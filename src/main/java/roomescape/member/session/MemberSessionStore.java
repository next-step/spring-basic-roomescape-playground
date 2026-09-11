package roomescape.member.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class MemberSessionStore {
    private static final String MEMBER_ID = "memberId";

    public void saveMemberId(HttpSession session, Long memberId) {
        session.setAttribute(MEMBER_ID, memberId);
    }

    public Long getMemberId(HttpSession session) {
        return (Long) session.getAttribute(MEMBER_ID);
    }
}
