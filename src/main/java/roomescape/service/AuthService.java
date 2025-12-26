package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.LoginRequest;
import roomescape.auth.JwtTokenProvider;
import roomescape.model.Member;

@Service
public class AuthService {
    private final MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String createToken(LoginRequest request) {
        Member member = memberService.authenticate(request.email(), request.password());

        return jwtTokenProvider.createToken(member);
    }
}
