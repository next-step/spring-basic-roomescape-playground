package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.jwt.TokenProvider;
import roomescape.exception.AuthorizationException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(),
                loginRequest.getPassword())
            .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));
        return tokenProvider.createToken(member);
    }

    public LoginMember login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));
        return new LoginMember(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getRole()
        );
    }
}
