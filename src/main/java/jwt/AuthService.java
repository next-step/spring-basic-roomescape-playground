package jwt;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginRequest;
import roomescape.auth.TokenResponse;
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public AuthService(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberService.authenticate(loginRequest.email(), loginRequest.password());
        String token = jwtProvider.createToken(member);
        return new TokenResponse(token);
    }

    public MemberResponse getMemberByToken(String token) {
        Long memberId = jwtProvider.extractMemberId(token);
        return memberService.getById(memberId);
    }
}
