package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.AuthorizationException;
import roomescape.member.LoginRequest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@RequiredArgsConstructor
public class JwtUtils {

    private final MemberRepository memberRepository;

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public String createToken(LoginRequest loginRequest) {
        if (!checkValidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new AuthorizationException();
        }

        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

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
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        return memberRepository.findById(memberId).get();
    }

    public Cookie createCookie(String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException();
    }
}