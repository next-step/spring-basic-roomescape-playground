package roomescape.provider;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.AuthorizationException;
import roomescape.member.LoginRequest;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class TokenProvider {
    private MemberDao memberDao;
    public String createAccessToken(LoginRequest loginRequest) {
        if (!checkValidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new AuthorizationException();
        }

        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return accessToken;


//    public String getMemberNameFromToken(String accessToken) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(accessToken)
//                .getBody();
//        return claims.get("name", String.class);
//    }
    }
    private boolean checkValidLogin(String principal, String credentials) {
        return memberDao.existByEmailAndPassword(principal, credentials);
    }
}