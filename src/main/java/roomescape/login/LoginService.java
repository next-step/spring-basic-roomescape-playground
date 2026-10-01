package roomescape.login;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class LoginService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(LoginAuthenticationException::new);

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new LoginAuthenticationException();
        }

        return Jwts.builder()
                   .setSubject(member.getId().toString())
                   .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                   .compact();
    }

    public Member findMemberByToken(String token) {
        try {
            Long memberId = Long.valueOf(
                    Jwts.parserBuilder()
                        .setSigningKey(
                                Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        )
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
            );

            return memberRepository
                    .findById(memberId)
                    .orElseThrow(LoginAuthenticationException::new);

        } catch (JwtException | NumberFormatException e) {
            throw new LoginAuthenticationException();
        }
    }
}
