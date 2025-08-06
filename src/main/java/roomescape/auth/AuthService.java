package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.jwt.JwtTokenExtractor;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Service
public class AuthService {
    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @Transactional
    public String createToken(LoginRequest loginRequest) {
        Member member = findMember(loginRequest);
        return tokenProvider.generateToken(member.getId(), member.getName(), member.getRole());
    }

    private Member findMember(LoginRequest loginRequest) {
        return memberService.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
    }

    @Transactional(readOnly = true)
    public Member findMemberByToken(HttpServletRequest request) {
        String token = JwtTokenExtractor.extract(request);
        validateExpireToken(token);
        Long memberId = tokenProvider.extractIdFromToken(token);
        return memberService.findById(memberId);
    }

    private void validateExpireToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
