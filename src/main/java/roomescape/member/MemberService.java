package roomescape.member;

import org.springframework.stereotype.Service;

import roomescape.global.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MemberService {
    private MemberDao memberDao;
    private TokenProvider tokenProvider;

    public MemberService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse login(final MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword());
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
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

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public String createToken(MemberResponse memberResponse){
        String accessToken = tokenProvider.createToken(new Member(memberResponse.getId(), memberResponse.getName(), memberResponse.getEmail(), "USER"));
        return accessToken;
    }

    public void createCookie(final HttpServletResponse response, final String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
