package roomescape.auth.client.cookie;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import roomescape.auth.AuthController;

class CookieProviderTest {

    private CookieProvider cookieProvider = new CookieProvider();

    @Nested
    class GenerateCookieTests {

        @Test
        void 기본_쿠키를_생성한다() {
            String value = "value";
            Duration duration =  Duration.ofMinutes(AuthController.DEFAULT_TIME);
            ResponseCookie cookie = cookieProvider.generateCookie(value, duration);

            assertThat(cookie.toString())
                    .contains("token=" + value)
                    .contains("Max-Age=" + duration.getSeconds())
                    .contains("Path=" + CookieProperties.PATH);
        }
    }
}
