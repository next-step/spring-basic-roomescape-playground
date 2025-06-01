package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.member.dto.LoginRequest;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member findMember = memberRepository
                .findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        return tokenProvider.createToken(findMember);
    }

    public Member loginCheck(String token) {
        Long memberId = tokenProvider.parse(token);

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    }
}
