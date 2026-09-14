package roomescape.member.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import roomescape.member.LoginMember;
import roomescape.member.session.MemberSessionStore;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberSessionStore memberSessionStore;

    public AdminInterceptor(MemberSessionStore memberSessionStore) {
        this.memberSessionStore = memberSessionStore;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        LoginMember loginMember = memberSessionStore.getLoginMember(session);
        if (loginMember == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        if (!loginMember.isAdmin()) {
            throw new MemberException(MemberErrorCode.ADMIN_REQUIRED);
        }

        return true;
    }

}
