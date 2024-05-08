package roomescape.provider;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.AuthorizationException;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class TokenProvider {
    private MemberDao memberDao;
    String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public String createAccessToken(Member loginRequest) {
        if (!checkValidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new AuthorizationException();
        }

        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        return accessToken;


    }
    private boolean checkValidLogin(String principal, String credentials) {
        return memberDao.existByEmailAndPassword(principal, credentials);
    }

//    public String extractMemberNameFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.get("name", String.class);
//    }

    public String getMemberFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("name", String.class);
    }
}