package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;


    @Test
    void 로그인_없이_예약을_생성할_수_있다() {
        // given
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        Theme savedTheme = themeRepository.save(theme);

        Time time = new Time(LocalTime.of(22, 0));
        Time savedTime = timeRepository.save(time);

        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberRepository.save(member);

        ReservationRequest request = new ReservationRequest(member.getName(), LocalDate.of(2025, 3, 15), savedTheme.getId(), savedTime.getId());
        // when
        ReservationResponse response = reservationService.save(request, null);
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(member.getName()),
                () -> assertThat(response.getTheme()).isEqualTo(theme.getName()),
                () -> assertThat(response.getDate()).isEqualTo(request.getDate()),
                () -> assertThat(response.getTime()).isEqualTo(time.getValue())
        );
    }

    @Test
    void 로그인_정보를_활용하여_예약을_생성할_수_있다() {
        // given
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        Theme savedTheme = themeRepository.save(theme);

        Time time = new Time(LocalTime.of(22, 0));
        Time savedTime = timeRepository.save(time);

        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberRepository.save(member);

        ReservationRequest request = new ReservationRequest(null, LocalDate.of(2025, 3, 15), savedTheme.getId(), savedTime.getId());
        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getName(), savedMember.getEmail(), savedMember.getRole());
        // when
        ReservationResponse response = reservationService.save(request, loginMember);
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(loginMember.name()),
                () -> assertThat(response.getTheme()).isEqualTo(theme.getName()),
                () -> assertThat(response.getDate()).isEqualTo(request.getDate()),
                () -> assertThat(response.getTime()).isEqualTo(time.getValue())
        );
    }

    @Test
    void 동일한_날짜_시간_및_테마를_가진_예약이_존재하면_예외가_발생한다() {
        // given
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        Theme savedTheme = themeRepository.save(theme);

        Time time = new Time(LocalTime.of(22, 0));
        Time savedTime = timeRepository.save(time);

        Member member1 = new Member("멤버1", "member1@email.com", "password", Role.USER);
        Member savedMember1 = memberRepository.save(member1);
        Member member2 = new Member("멤버2", "member2@email.com", "password", Role.USER);
        Member savedMember2 = memberRepository.save(member2);

        ReservationRequest request1 = new ReservationRequest(null, LocalDate.of(2025, 3, 15), savedTheme.getId(), savedTime.getId());
        LoginMember loginMember1 = new LoginMember(savedMember1.getId(), savedMember1.getName(), savedMember1.getEmail(), savedMember1.getRole());
        reservationService.save(request1, loginMember1);

        ReservationRequest request2 = new ReservationRequest(null, LocalDate.of(2025, 3, 15), savedTheme.getId(), savedTime.getId());
        LoginMember loginMember2 = new LoginMember(savedMember2.getId(), savedMember2.getName(), savedMember2.getEmail(), savedMember2.getRole());
        // when & then
        assertThatThrownBy(() -> reservationService.save(request2, loginMember2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.RESERVATION_ALREADY_EXISTS.getMessage());
    }
}
