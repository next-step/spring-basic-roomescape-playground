package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;
import org.springframework.stereotype.Service;
import roomescape.provider.TokenProvider;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findMemberByEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
    public String createToken(MemberResponse member) {
        String accessToken = TokenProvider.createToken(member);
        return accessToken;
    }
    String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    public MemberResponse findByToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject());
        Member member = memberDao.findById(memberId);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
