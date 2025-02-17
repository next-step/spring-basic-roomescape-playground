package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import auth.JwtAuthManager;

@Service
public class AuthService {
    private final JwtAuthManager jwtAuthManager;
    private final MemberRepository memberRepository;

    public AuthService(JwtAuthManager jwtAuthManager, MemberRepository memberRepository) {
        this.jwtAuthManager = jwtAuthManager;
        this.memberRepository = memberRepository;
    }

    public String createToken(String email, String password) {
        return jwtAuthManager.createToken(email, password);
    }

    public Long extractMemberId(String token) {
        return jwtAuthManager.getId(token);
    }

    public String extractRole(String token) {
        return jwtAuthManager.getRole(token);
    }

    public void validateToken(String token) {
        jwtAuthManager.validateToken(token);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 멤버가 존재하지 않습니다."));
    }
}
