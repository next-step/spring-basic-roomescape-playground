package roomescape;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.member.AuthorizationException;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Service
public class AuthService {
    private final CookieUtil cookieUtil;
    private final MemberService memberService;

    public AuthService(CookieUtil cookieUtil, MemberService memberService) {
        this.cookieUtil = cookieUtil;
        this.memberService = memberService;
    }

    public Member findAuthenticatedMember(HttpServletRequest request) {
        String token = cookieUtil.extractToken(request);
        if (token.isEmpty()) {
            throw new AuthorizationException("쿠키에 토큰이 없습니다.");
        }
        return memberService.findMemberByToken(token);
    }
}
