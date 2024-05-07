package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findMember(String email, String password){
        Member member=memberDao.findByEmailAndPassword(email,password);
        return member;
    }

    public Member findByToken(String token){
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        Member member = memberDao.findById(memberId);
        return member;

    }

    public String createToken(Member member){
        String accessToken = jwtTokenProvider.createToken(member);
        return accessToken;
    }

    public void createCookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie("token", token); //생성한 토큰으로 쿠키를 생성
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) { //쿠키 배열에서 원하는 토큰을 추출합니다.
                return cookie.getValue();
            }
        }

        return "";
    }
}
