package roomescape.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.DataBaseCleaner;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.controller.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(DataBaseCleaner.class)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void 로그인을_할_수_있다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberDao.save(member);
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());
        // when
        LoginResponse loginResponse = authService.login(loginRequest);
        // then
        assertThat(loginResponse.accessToken()).isNotBlank();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이메일이_비어있으면_예외가_발생한다(String email) {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberDao.save(member);
        // when & then
        assertThatThrownBy(() -> authService.login(new LoginRequest(email, member.getPassword())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.INVALID_EMAIL.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비밀번호가_비어있으면_예외가_발생한다(String password) {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberDao.save(member);
        // when & then
        assertThatThrownBy(() -> authService.login(new LoginRequest(member.getEmail(), password)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.INVALID_PASSWORD.getMessage());
    }

    @Test
    void 인증_정보를_조회할_수_있다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberDao.save(member);
        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getName(), savedMember.getEmail(), Role.USER);
        // when
        LoginCheckResponse loginCheckResponse = authService.loginCheck(loginMember);
        // then
        assertThat(loginCheckResponse.name()).isEqualTo(savedMember.getName());
    }

    @Test
    void 로그인_한_멤버를_조회할_수_있다() {
        // given
        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberDao.save(member);
        String accessToken = jwtTokenProvider.createAccessToken(savedMember);
        // when
        Member loginMember = authService.getLoginMember(accessToken);
        // then
        assertAll(
                () -> assertThat(loginMember.getId()).isEqualTo(savedMember.getId()),
                () -> assertThat(loginMember.getName()).isEqualTo(savedMember.getName()),
                () -> assertThat(loginMember.getEmail()).isEqualTo(savedMember.getEmail()),
                () -> assertThat(loginMember.getRole()).isEqualTo(savedMember.getRole())
        );
    }

    @Test
    void 로그인시_유효한_토큰이_아니면_예외가_발생한다() {
        //given & when
        String invalidToken = "invalid.token";
        //then
        assertThatThrownBy(() -> authService.getLoginMember(invalidToken))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(ExceptionMessage.INVALID_TOKEN.getMessage());
    }
}

