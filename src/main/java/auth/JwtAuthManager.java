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

    public String createToken(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("유효한 이메일이 아닙니다."));

        String role = member.getRole();
        String name = member.getName();
        Long memberId = member.getId();

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("name", name);
        claims.put("memberId", memberId);

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
        Object memberId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("memberId");

        if (memberId instanceof Double) {
            return ((Double) memberId).longValue();
        } else if (memberId instanceof Long) {
            return (Long) memberId;
        } else {
            throw new IllegalArgumentException("유효한 memberId 형식이 아닙니다.");
        }
    }

    public String getName(String token) {
        return (String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("name");
    }

    public String getEmail(String token) {
        return (String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("email");
    }

    public String getRole(String token) {
        return (String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role");
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
