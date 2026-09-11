package roomescape.member;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    @Test
    void token_has_expiration_time() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(SECRET_KEY, 3_600_000);

        TokenPayload tokenPayload = tokenProvider.parseToken(tokenProvider.createToken(1L));

        assertThat(tokenPayload.expiresAt()).isAfter(Instant.now());
    }

    @Test
    void expired_token_is_rejected() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(SECRET_KEY, -1_000);
        String expiredToken = tokenProvider.createToken(1L);

        assertThatThrownBy(() -> tokenProvider.parseToken(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
