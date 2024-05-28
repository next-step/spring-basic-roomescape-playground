package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.AuthorizationException;
import roomescape.member.LoginRequest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final MemberRepository memberRepository;

    @Value("${roomescape.auth.jwt.secret}")
    private String secret;

    public String createToken(LoginRequest loginRequest) {
        if (!checkValidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new AuthorizationException();
        }

        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        String secretKey = secret;
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return accessToken;
    }

    public boolean checkValidLogin(String principal, String credentials) {
        return memberRepository.existsByEmailAndPassword(principal, credentials);
    }

    public Member extractMemberFromToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());

        return memberRepository.findById(memberId).get();
    }

}
