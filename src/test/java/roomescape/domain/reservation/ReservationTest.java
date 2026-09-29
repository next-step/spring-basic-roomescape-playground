package roomescape.domain.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.entity.Member;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationTest {

    private final Member alice = new Member("Alice", "alice@dummy.com", "dummy", "USER");
    private final LocalDate date = LocalDate.now().plusDays(1);
    private final Time time = new Time(LocalTime.of(10, 0));
    private final Theme theme = new Theme("dummy", "dummy");

    @Test
    void Reservation은_member가_빈_채로_생성할_수_없다() {

        // member == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(date, null, time, theme)
        );
    }

    @Test
    void Reservation은_date가_빈_채로_생성할_수_없다() {

        // date == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(null, alice, time, theme)
        );
    }

    @Test
    void Reservation은_과거의_날짜로_생성할_수_없다() {

        // date < today
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(LocalDate.now().minusDays(1), alice, time, theme)
        );
    }

    @Test
    void Reservation은_time이_빈_채로_생성할_수_없다() {

        // time == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(date, alice,  null, theme)
        );
    }

    @Test
    void Reservation은_theme이_빈_채로_생성할_수_없다() {

        // theme == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(date, alice, time, null)
        );
    }

    @Test
    void Reservation을_정상적으로_생성한_경우() {
        // given
        Reservation reservation = new Reservation(date, alice,  time, theme);

        // then
        assertThat(reservation.getMember().getNickname()).isEqualTo(alice.getNickname());
        assertThat(reservation.getDate()).isEqualTo(date);
    }
}
