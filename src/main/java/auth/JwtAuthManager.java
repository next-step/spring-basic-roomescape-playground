package auth;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.exception.AuthorizationException;

import java.util.Date;

public class JwtAuthManager {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Value("${roomescape.auth.jwt.expire-length}")
    private long validityInMilliseconds;

    private final MemberRepository memberRepository;

    public JwtAuthManager(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createToken(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new AuthorizationException("유효한 이메일이 아닙니다."));

        Long memberId = member.getId();
        String role = member.getRole();

        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getId(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getRole(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }

    public void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {

                throw new IllegalArgumentException("토큰이 만료되었습니다.");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", e);
        }
    }
}
