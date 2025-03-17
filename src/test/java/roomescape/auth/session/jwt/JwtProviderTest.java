package roomescape.auth.session.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
    }

    @Nested
    class generateToken {
        @Test
        void 토큰을_생성을_적절히_생성한다() {
            Long userId = 1L;
            String userName = "어드민";
            String userRole = "ADMIN";

            String token = jwtProvider.generateToken(userId, userName, userRole);

            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(JwtProperties.SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            assertThat(parsedToken.getBody().getSubject()).isEqualTo(userId.toString());
            assertThat(parsedToken.getBody().get(JwtProperties.NAME)).isEqualTo(userName);
            assertThat(parsedToken.getBody().get(JwtProperties.ROLE)).isEqualTo(userRole);
        }
    }
}
