package roomescape.auth;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        return jwtTokenProvider.createToken(member);
    }

    public LoginMember createAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        return new LoginMember(
                JwtTokenProvider.extract(claims, "sub"),
                JwtTokenProvider.extract(claims, "name"),
                Role.valueOf(JwtTokenProvider.extract(claims, "role")));
    }
}
