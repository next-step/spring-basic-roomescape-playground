package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.exception.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminInterceptorTest {
    @Mock
    private MemberService memberService;
    private AdminInterceptor adminInterceptor;

    @BeforeEach
    void setUp() {
        adminInterceptor = new AdminInterceptor(memberService);
    }

    @Test
    void 쿠키가_없으면_예외가_발생한다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when & then
        assertThatThrownBy(() -> adminInterceptor.preHandle(request, response, null))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 일반_유저_권한이면_401_상태코드를_반환하고_요청을_차단한다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", "userToken"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        Member userMember = new Member(1L, "일반유저", "user@email.com", "USER");
        given(memberService.findMemberByToken("userToken")).willReturn(userMember);

        // when
        boolean result = adminInterceptor.preHandle(request, response, null);

        // then
        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(MockHttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void 관리자_권한이면_요청을_통과시킨다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", "adminToken"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        Member adminMember = new Member(2L, "관리자", "admin@email.com", "ADMIN");
        given(memberService.findMemberByToken("adminToken")).willReturn(adminMember);

        // when
        boolean result = adminInterceptor.preHandle(request, response, null);

        // then
        assertThat(result).isTrue();
    }
}
