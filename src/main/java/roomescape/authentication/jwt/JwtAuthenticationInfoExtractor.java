package roomescape.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import roomescape.authentication.AuthenticationExtractor;
import roomescape.authentication.AuthenticationResponse;
import roomescape.authentication.MemberAuthInfo;
import roomescape.exception.JwtValidationException;

import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationInfoExtractor implements AuthenticationExtractor {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public MemberAuthInfo extractMemberAuthInfoFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtValidationException("토큰이 존재하지 않습니다.");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = Long.valueOf(claims.getSubject());
            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);

            return new MemberAuthInfo(id, name, role);
        } catch (JwtException e) {
            throw new JwtValidationException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    public AuthenticationResponse extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new JwtValidationException("쿠키가 존재하지 않습니다.");
        }

        try {
            String accessToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("token"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElseThrow(() -> new JwtValidationException("토큰이 존재하지 않습니다."));

            return new AuthenticationResponse(accessToken);
        } catch (Exception e) {
            throw new JwtValidationException("쿠키에서 토큰 추출 중 오류가 발생했습니다.", e);
        }
    }
}
