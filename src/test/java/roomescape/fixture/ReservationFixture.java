package roomescape.fixture;

import roomescape.member.entity.Member;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;

import java.time.LocalDate;

@SuppressWarnings("NonAsciiCharacters")
public class ReservationFixture {

    public static Reservation 내일_예약_생성(
            Member member,
            Time time,
            Theme theme
    ) {
        return Reservation.of(member, LocalDate.now().plusDays(1), time, theme);
    }

    public static Reservation 날짜_지정_예약_생성(
            Member member,
            LocalDate date,
            Time time,
            Theme theme
    ) {
        return Reservation.of(member, date, time, theme);
    }
}
