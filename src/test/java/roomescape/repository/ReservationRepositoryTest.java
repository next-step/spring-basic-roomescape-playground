package roomescape.repository;

import org.junit.jupiter.api.Test;
import roomescape.member.entity.Member;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.MemberFixture.멤버_멤버1_생성;
import static roomescape.fixture.MemberFixture.멤버_멤버2_생성;
import static roomescape.fixture.ReservationFixture.날짜_지정_예약_생성;
import static roomescape.fixture.ThemeFixture.테마_테마1_생성;
import static roomescape.fixture.ThemeFixture.테마_테마2_생성;
import static roomescape.fixture.TimeFixture.시간_시간1_생성;
import static roomescape.fixture.TimeFixture.시간_시간2_생성;

@SuppressWarnings("NonAsciiCharacters")
public class ReservationRepositoryTest extends RepositoryTest {

    @Test
    void 특정_회원의_예약만_조회한다() {
        // given
        Member member1 = 멤버_멤버1_생성();
        Member member2 = 멤버_멤버2_생성();
        복수_멤버_저장(member1, member2);

        Time time1 = 시간_시간1_생성();
        Time time2 = 시간_시간2_생성();
        복수_시간_저장(time1, time2);

        Theme theme1 = 테마_테마1_생성();
        Theme theme2 = 테마_테마2_생성();
        복수_테마_저장(theme1, theme2);

        LocalDate date = LocalDate.now().plusDays(1);

        Reservation member1Reservation1 =
                날짜_지정_예약_생성(member1, date, time1, theme1);
        Reservation member1Reservation2 =
                날짜_지정_예약_생성(member1, date, time2, theme1);
        Reservation member2Reservation =
                날짜_지정_예약_생성(member2, date, time1, theme2);
        복수_예약_저장(
                member1Reservation1,
                member1Reservation2,
                member2Reservation
        );

        // when
        List<Reservation> actual =
                reservationRepository.findAllByMemberId(member1.getId());

        // then
        assertThat(actual)
                .extracting(Reservation::getId)
                .containsExactlyInAnyOrder(
                        member1Reservation1.getId(),
                        member1Reservation2.getId()
                );
    }

    @Test
    void 날짜와_테마가_모두_일치하는_예약만_조회한다() {
        // given
        Member member = 멤버_멤버1_생성();
        단일_멤버_저장(member);

        Time time = 시간_시간1_생성();
        단일_시간_저장(time);

        Theme targetTheme = 테마_테마1_생성();
        Theme otherTheme = 테마_테마2_생성();
        복수_테마_저장(targetTheme, otherTheme);

        LocalDate targetDate = LocalDate.now().plusDays(1);
        LocalDate otherDate = targetDate.plusDays(1);

        Reservation matchingReservation =
                날짜_지정_예약_생성(member, targetDate, time, targetTheme);

        Reservation differentDateReservation =
                날짜_지정_예약_생성(member, otherDate, time, targetTheme);

        Reservation differentThemeReservation =
                날짜_지정_예약_생성(member, targetDate, time, otherTheme);

        복수_예약_저장(
                matchingReservation,
                differentDateReservation,
                differentThemeReservation
        );

        // when
        List<Reservation> actual =
                reservationRepository.findByDateAndThemeId(
                        targetDate,
                        targetTheme.getId()
                );

        // then
        assertThat(actual)
                .extracting(Reservation::getId)
                .containsExactly(matchingReservation.getId());
    }

    @Test
    void 같은_예약_조건이_존재하면_true를_반환한다() {
        // given
        Member member = 멤버_멤버1_생성();
        단일_멤버_저장(member);

        Time time = 시간_시간1_생성();
        단일_시간_저장(time);

        Theme theme = 테마_테마1_생성();
        단일_테마_저장(theme);

        LocalDate date = LocalDate.now().plusDays(1);

        Reservation reservation =
                날짜_지정_예약_생성(member, date, time, theme);

        단일_예약_저장(reservation);

        // when
        boolean actual =
                reservationRepository.existsByDateAndTimeAndTheme(
                        date,
                        time,
                        theme
                );

        // then
        assertThat(actual).isTrue();
    }
}
