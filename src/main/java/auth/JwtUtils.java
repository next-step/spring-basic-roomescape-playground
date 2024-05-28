package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.Member;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtils {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;
    public String createToken( Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role",member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact(); //jwt builder로 token 생성
        return accessToken; //시크릿키에 담아 반환
    }

    public String extractSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String extractClaim(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().get(key).toString();
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
