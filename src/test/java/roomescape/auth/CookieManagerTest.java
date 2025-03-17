package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.auth.controller.AuthController;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CookieManagerTest {

    @Test
    void 쿠키가_존재하지_않는_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new CookieManager(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
    }

    @Test
    void 쿠키를_생성할_수_있다() {
        // given & when
        String cookieValue = "value";
        Cookie cookie = CookieManager.createCookie(AuthController.AUTH_TOKEN_COOKIE, cookieValue);
        // then
        assertAll(
                () -> assertThat(cookie.getName()).isEqualTo(AuthController.AUTH_TOKEN_COOKIE),
                () -> assertThat(cookie.getValue()).isEqualTo(cookieValue)
        );
    }

    @Test
    void 쿠키_값을_조회할_수_있다() {
        // given
        String cookieName = "cookieName";
        String cookieValue = "accessToken";
        Cookie cookie = new Cookie(cookieName, cookieValue);
        CookieManager cookieManager = new CookieManager(new Cookie[]{cookie});
        // when
        String resultValue = cookieManager.getValue(cookieName);
        // then
        assertThat(resultValue).isEqualTo(cookieValue);
    }

    @Test
    void 요청한_이름의_쿠키가_존재하지_않는_경우_예외가_발생한다() {
        // given
        Cookie cookie = new Cookie("invalid_name", "accessToken");
        CookieManager cookieManager = new CookieManager(new Cookie[]{cookie});
        // when & then
        assertThatThrownBy(() -> cookieManager.getValue(AuthController.AUTH_TOKEN_COOKIE))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 쿠키_값이_존재하지_않는_경우_예외가_발생한다(String accessToken) {
        // given
        Cookie cookie = new Cookie(AuthController.AUTH_TOKEN_COOKIE, accessToken);
        CookieManager cookieManager = new CookieManager(new Cookie[]{cookie});
        // when & then
        assertThatThrownBy(() -> cookieManager.getValue(AuthController.AUTH_TOKEN_COOKIE))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.INVALID_COOKIE_VALUE.getMessage());
    }
}
