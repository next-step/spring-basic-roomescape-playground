package roomescape.authentication.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import roomescape.authentication.AuthenticationProvider;
import roomescape.authentication.AuthenticationResponse;
import roomescape.exception.JwtProviderException;
import roomescape.member.Member;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Override
    public AuthenticationResponse createAuthenticationMethod(Member member) {
        try {
            String accessToken = Jwts.builder()
                    .setSubject(member.getId().toString())
                    .claim("name", member.getName())
                    .claim("role", member.getRole())
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();

            return new AuthenticationResponse(accessToken);
        } catch (JwtException e) {
            throw new JwtProviderException("JWT 생성에 실패하였습니다.", e);
        }
    }
}
