package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.DataBaseCleaner;
import roomescape.auth.dto.LoginMember;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(DataBaseCleaner.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private TimeDao timeDao;


    @Test
    void 쿠키_값인_토큰으로_멤버를_조회하여_예약을_생성할_수_있다() {
        // given
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        Theme savedTheme = themeDao.save(theme);

        Time time = new Time(LocalTime.of(22,0));
        Time savedTime = timeDao.save(time);

        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        memberDao.save(member);

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
        Theme savedTheme = themeDao.save(theme);

        Time time = new Time(LocalTime.of(22,0));
        Time savedTime = timeDao.save(time);

        Member member = new Member("멤버", "member@email.com", "password", Role.USER);
        Member savedMember = memberDao.save(member);

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
}
