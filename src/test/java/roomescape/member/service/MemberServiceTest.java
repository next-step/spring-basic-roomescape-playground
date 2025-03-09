package roomescape.member.service;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.BadRequestException;
import roomescape.common.exception.ExceptionMessage;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;
import roomescape.member.util.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static roomescape.member.controller.MemberController.COOKIE_NAME;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 로그인을_할_수_있다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        memberDao.save(member);
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());
        // when
        LoginResponse loginResponse = memberService.login(loginRequest);
        // then
        assertThat(loginResponse.accessToken()).isNotBlank();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이메일이_비어있으면_예외가_발생한다(String email) {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        memberDao.save(member);
        // when & then
        assertThatThrownBy(() -> memberService.login(new LoginRequest(email, member.getPassword())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.INVALID_EMAIL.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비밀번호가_비어있으면_예외가_발생한다(String password) {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        memberDao.save(member);
        // when & then
        assertThatThrownBy(() -> memberService.login(new LoginRequest(member.getEmail(), password)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.INVALID_PASSWORD.getMessage());
    }

    @Test
    void 인증_정보를_조회할_수_있다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        Member savedMember = memberDao.save(member);
        String accessToken = jwtTokenProvider.createToken(savedMember);
        Cookie cookie = new Cookie(COOKIE_NAME, accessToken);
        // when
        LoginCheckResponse loginCheckResponse = memberService.loginCheck(cookie);
        // then
        assertThat(loginCheckResponse.name()).isEqualTo(member.getName());
    }

    @Test
    void 인증_정보_조회_시_토큰이_유효하지_않은_경우_예외가_발생한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        memberDao.save(member);
        String invalidAccessToken = "invalid.token";
        Cookie cookie = new Cookie(COOKIE_NAME, invalidAccessToken);
        // when & then
        assertThatThrownBy(() -> memberService.loginCheck(cookie))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.INVALID_TOKEN.getMessage());
    }

    @Test
    void 인증_정보_조회_시_쿠키가_존재하지_않는_경우_예외가_발생한다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        memberDao.save(member);
        Cookie cookie = null;
        // when & then
        assertThatThrownBy(() -> memberService.loginCheck(cookie))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 인증_정보_조회_시_쿠키_값이_존재하지_않는_경우_예외가_발생한다(String accessToken) {
        // given
        Member member = new Member("멤버", "member@email.com", "password", "USER");
        memberDao.save(member);
        Cookie cookie = new Cookie(COOKIE_NAME, accessToken);
        // when & then
        assertThatThrownBy(() -> memberService.loginCheck(cookie))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
    }
}
