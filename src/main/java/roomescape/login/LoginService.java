package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class LoginService {

    private final MemberDao memberDao;

    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        String accessToken = Jwts.builder()
                                 .setSubject(member.getId().toString())
                                 .claim("name", member.getName())
                                 .claim("role", member.getRole())
                                 .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                                 .compact();

        return accessToken;
    }

    public Member findMemberByToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                                         .setSigningKey(Keys.hmacShaKeyFor(
                                                 "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()
                                         ))
                                         .build()
                                         .parseClaimsJws(token)
                                         .getBody()
                                         .getSubject());

        return memberDao.findById(memberId);
    }
}
