package roomescape.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        return jwtTokenProvider.createToken(member);
    }


    public Member findMemberByToken(String token) {
        Long memberId = jwtTokenProvider.extractMemberIdFromToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 토큰입니다."));
    }
}
