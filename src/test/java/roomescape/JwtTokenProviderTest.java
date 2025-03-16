package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.JwtTokenProvider;

@SpringBootTest
@Transactional
public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Value("${roomescape.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", validityInMilliseconds);
    }

    @Test
    @DisplayName("토큰이_올바르게_생성된다")
    void createTokenTest() {
        // given
        String userEmail = "test@roomescape.com";

        // when
        String token = jwtTokenProvider.createToken(userEmail);

        // then
        assertThat(token).isNotEmpty();
        assertThat(Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token).getBody()
                .get("email", String.class))
                .isEqualTo(String.valueOf(userEmail));
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

    @DisplayName("만료된_토큰으로_조회할_경우_예외를_발생시킨다.")
    @Test
    void getPayloadByExpiredToken() {
        final String expiredToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .setSubject(String.valueOf(1L))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .compact();

        assertThatExceptionOfType(ExpiredJwtException.class)
                .isThrownBy(() -> jwtTokenProvider.getEmailFromToken(expiredToken));
    }


}
