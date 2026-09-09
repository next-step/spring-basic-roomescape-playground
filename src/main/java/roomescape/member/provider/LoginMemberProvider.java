package roomescape.member.provider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import roomescape.member.LoginMember;
import roomescape.member.MemberService;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;

@Component
public class LoginMemberProvider {
    private final MemberService memberService;

    public LoginMemberProvider(MemberService memberService) {
        this.memberService = memberService;
    }

    public LoginMember getLoginMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        return LoginMember.from(memberService.getMember(memberId));
    }
}
