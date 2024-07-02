package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class AuthorizationExtractor {
    private String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private MemberDao memberDao;

    public AuthorizationExtractor(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long extractMemberId (String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        return memberId;
    }


}

