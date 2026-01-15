package roomescape.auth;

import io.jsonwebtoken.JwtException;
import missionAuth.JwtDto;
import missionAuth.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, JwtUtils jwtTokenProvider, PasswordEncoder passwordEncoder) {
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

        return jwtTokenProvider.createToken(member.getId(), member.getName(), member.getRole());
    }


    public JwtDto parseToken(String token) {
        try {
            return jwtTokenProvider.parse(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
