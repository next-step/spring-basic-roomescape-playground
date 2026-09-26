package roomescape.reservation.domain;

import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {
    private final LocalDate date = LocalDate.of(2024, 3, 1);
    private final Time time = new Time(1L, LocalTime.of(10, 0));
    private final Theme theme = new Theme(1L, "테마", "설명");

    @Test
    void member_reservation_keeps_member_and_reserver_name() {
        Member member = new Member(1L, "브라운", "brown@email.com", "password", "USER");

        Reservation reservation = Reservation.byMember(member, date, time, theme);

        assertThat(reservation.getMember()).isSameAs(member);
        assertThat(reservation.getName()).isEqualTo("브라운");
    }

    @Test
    void non_member_reservation_keeps_only_reserver_name() {
        Reservation reservation = Reservation.byName("전화 예약자", date, time, theme);

        assertThat(reservation.getMember()).isNull();
        assertThat(reservation.getName()).isEqualTo("전화 예약자");
    }

    @Test
    void reserver_name_cannot_be_blank() {
        assertThatThrownBy(() -> Reservation.byName(" ", date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 필수입니다.");
    }
}
