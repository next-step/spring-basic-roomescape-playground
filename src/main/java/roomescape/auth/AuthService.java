package roomescape.auth;

import io.jsonwebtoken.JwtException;
import missionAuth.JwtDto;
import missionAuth.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.exception.FailMessage;
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
                .orElseThrow(() -> new UnauthorizedException(FailMessage.AUTH_LOGIN_FAILED));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnauthorizedException(FailMessage.AUTH_LOGIN_FAILED);
        }

        return jwtTokenProvider.createToken(member.getId(), member.getName(), member.getRole());
    }


    public JwtDto parseToken(String token) {
        try {
            return jwtTokenProvider.parse(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(FailMessage.AUTH_INVALID_TOKEN);
        }
    }
}
