package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.MemberService;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public LoginMember findLoginMemberByToken(String token) {
        Long memberId = jwtTokenProvider.extractMemberId(token);
        return memberService.findLoginMemberById(memberId);
    }

    public boolean isAdmin(LoginMember loginMember) {
        return loginMember != null && "ADMIN".equals(loginMember.getRole());
    }
}
