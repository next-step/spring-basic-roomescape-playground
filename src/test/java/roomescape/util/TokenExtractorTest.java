package roomescape.util;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import roomescape.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TokenExtractorTest {

    @Test
    @DisplayName("정상적으로 토큰을 추출한다")
    void 정상적으로_토큰을_추출한다() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", "token_value"));

        //when
        String token = TokenExtractor.extractTokenFromCookie(request);

        //then
        assertThat(token).isEqualTo("token_value");
    }

    @Test
    @DisplayName("쿠키가 존재하지 않으면 예외를 던진다")
    void 쿠키가_존재하지_않으면_예외를_던진다() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies((Cookie[]) null);

        //then
        assertThatThrownBy(() -> TokenExtractor.extractTokenFromCookie(request))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @Test
    @DisplayName("토큰 쿠키가 없으면 예외를 던진다")
    void 토큰_쿠키가_없으면_예외를_던진다() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("cookie","cookiecookie"));

        //then
        assertThatThrownBy(() -> TokenExtractor.extractTokenFromCookie(request))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
