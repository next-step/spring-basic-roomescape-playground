package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.AuthRequest;
import roomescape.auth.dto.AuthResponse;
import roomescape.auth.dto.MemberDetailResponse;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(AuthRequest authRequest) {
        Member foundMember = memberRepository.findByEmailAndPassword(authRequest.email(), authRequest.password());
        String accessToken = jwtTokenProvider.createToken(foundMember);

        return new AuthResponse(accessToken);
    }

    public MemberDetailResponse checkLogin(String token) {
        Long memberId = jwtTokenProvider.getMemberId(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));

        return new MemberDetailResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
