package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final String secretKey;

    public MemberService(MemberDao memberDao, @Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.memberDao = memberDao;
        this.secretKey = secretKey;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findMemberByToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long id = claims.get("id", Long.class);
        return memberDao.findById(id);
    }
}
