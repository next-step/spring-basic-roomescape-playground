package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import roomescape.auth.CookieTokenExtractor;
import roomescape.auth.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class MemberControllerTest {
    @Test
    void 토큰이_없으면_검증과_조회_전에_명시적으로_거부한다() {
        MemberService service = mock(MemberService.class);
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        MemberController controller = new MemberController(service, provider, new CookieTokenExtractor());
        Cookie[][] cases = {
                null,
                new Cookie[]{new Cookie("language", "ko")},
                new Cookie[]{new Cookie("token", "")}
        };

        for (Cookie[] cookies : cases) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(cookies);

            assertThatThrownBy(() -> controller.getCurrentMember(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("로그인 토큰이 없습니다.");
        }
        verifyNoInteractions(service, provider);
    }
}
