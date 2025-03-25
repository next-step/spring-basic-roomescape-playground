package roomescape.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final String secretKey;

    public LoginService(MemberRepository memberRepository, @Value("${roomescape.auth.jwt.secret}") String secreteKey) {
        this.memberRepository = memberRepository;
        this.secretKey = secreteKey;
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginCheckResponse getUserInfoFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String memberName = claims.get("name", String.class);
        Member member = memberRepository.findByName(memberName);
        return new LoginCheckResponse(member.getName());
    }
}
