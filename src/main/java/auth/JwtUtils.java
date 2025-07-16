package auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

public class JwtUtils {

    private final Key key;
    private final MemberRepository memberRepo;

    public JwtUtils(String secret, MemberRepository memberRepo) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.memberRepo = memberRepo;
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(key)
                .compact();
    }

    public String createToken(String email, String password) {
        Member member = memberRepo
                .findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다."));
        return createToken(member);
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
