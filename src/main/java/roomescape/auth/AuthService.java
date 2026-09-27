package roomescape.auth;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import roomescape.exception.InvalidAuthenticationException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(
                        loginRequest.email(),
                        loginRequest.password()
                )
                .orElseThrow(InvalidAuthenticationException::new);

        return jwtTokenProvider.createToken(member);
    }

    public LoginMember findMemberByToken(String token) {
        try {
            Long memberId = jwtTokenProvider.extractMemberId(token);

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(InvalidAuthenticationException::new);

            return new LoginMember(member.getId(), member.getName(), member.getRole());
        } catch (JwtException exception) {
            throw new InvalidAuthenticationException();
        }
    }
}
