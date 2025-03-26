package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final String secretKey;

    public MemberService(MemberRepository memberRepository, @Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.memberRepository = memberRepository;
        this.secretKey = secretKey;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findMemberByToken(String token) {
        Claims claims = parseClaims(token);

        Double idDouble = claims.get("id", Double.class);
        Long id = null;
        if (idDouble != null) {
            id = idDouble.longValue();
        }

        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElse(null);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("id", member.getId())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
