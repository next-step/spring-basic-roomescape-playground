package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.security.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰이_올바르게_생성된다")
    void createTokenTest() {
        // given
        String userEmail = "test@roomescape.com";

        // when
        String token = jwtTokenProvider.createToken(userEmail);

        // then
        assertAll(
                () -> assertThat(token).isNotEmpty(),
                () -> assertThat(Jwts.parserBuilder()
                        .setSigningKey(jwtTokenProvider.getKey())
                        .build()
                        .parseClaimsJws(token).getBody()
                        .get("email", String.class))
                        .isEqualTo(userEmail)
        );
    }


    @Test
    @DisplayName("토큰에서_유저_아이디를_가져온다")
    void getIdFromTokenTest() {
        // given
        String userEmail = "test@roomescape.com";
        String token = jwtTokenProvider.createToken(userEmail);

        // when
        String extractedUserEmail = jwtTokenProvider.getEmailFromToken(token);

        // then
        assertThat(extractedUserEmail).isEqualTo(userEmail);
    }

    @Test
    @DisplayName("유효하지_않은_토큰으로_유저_아이디를_가져오면_예외가_발생한다")
    void getPayloadByInvalidToken() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> jwtTokenProvider.getEmailFromToken(null));
    }

    @Test
    @DisplayName("만료된_토큰으로_조회할_경우_예외를_발생시킨다.")
    void getPayloadByExpiredToken() {
        final String expiredToken = Jwts.builder()
                .signWith(jwtTokenProvider.getKey(), SignatureAlgorithm.HS256)
                .setSubject(String.valueOf(1L))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .compact();

        assertThatExceptionOfType(ExpiredJwtException.class)
                .isThrownBy(() -> jwtTokenProvider.getEmailFromToken(expiredToken));
    }
}
