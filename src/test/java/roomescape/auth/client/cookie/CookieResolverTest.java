package roomescape.auth.client.cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.RoomescapeUnauthorizedException;

public class CookieResolverTest {

    private CookieResolver cookieResolver;

    @BeforeEach
    void setUp() {
        cookieResolver = new CookieResolver();
    }

    @Nested
    class getTokenTest {
        @Test
        void 쿠키에서_토큰을_정상적으로_가져온다() {
            Cookie tokenCookie = new Cookie(CookieProperties.TOKEN, "testToken");
            Cookie[] cookies = {tokenCookie};

            String token = cookieResolver.getToken(cookies);

            assertThat(token).isEqualTo("testToken");
        }

        @Test
        void 쿠키에_토큰이_없으면_예외를_던진다() {
            Cookie[] cookies = {new Cookie("otherCookie", "someValue")};

            assertThatThrownBy(() -> cookieResolver.getToken(cookies))
                    .isInstanceOf(RoomescapeUnauthorizedException.class)
                    .hasMessage("로그인 되어있지 않습니다.");
        }

        @Test
        void 쿠키가_비어있으면_예외를_던진다() {
            Cookie[] cookies = {};

            assertThatThrownBy(() -> cookieResolver.getToken(cookies))
                    .isInstanceOf(RoomescapeUnauthorizedException.class)
                    .hasMessage("로그인 되어있지 않습니다.");
        }
    }
}
