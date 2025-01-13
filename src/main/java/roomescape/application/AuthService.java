package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public Member findLoginMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));
    }

    public String getEmailFromToken(String token) {
        return jwtTokenProvider.getPayload(token);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public void checkInvalidLogin(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new AuthorizationException("이메일 또는 비밀번호가 잘못되었습니다."));
    }

    public void verifyToken(String token) {
        jwtTokenProvider.validateToken(token);
    }
}
