package roomescape.domain.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationTest {

    private final String name = "Alice";
    private final LocalDate date = LocalDate.now().plusDays(1);
    private final Time time = new Time(1L, LocalTime.of(10, 0));
    private final Theme theme = new Theme(1L, "테마", "설명");

    @Test
    void Reservation은_name이_빈_채로_생성할_수_없다() {

        // name == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(null, date, time, theme)
        );

        // name == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("", date, time, theme)
        );

        // name == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(" ", date, time, theme)
        );
    }

    @Test
    void Reservation은_date가_빈_채로_생성할_수_없다() {

        // date == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(name, null, time, theme)
        );
    }

    @Test
    void Reservation은_과거의_날짜로_생성할_수_없다() {

        // date < today
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(name, LocalDate.now().minusDays(1), time, theme)
        );
    }

    @Test
    void Reservation은_time이_빈_채로_생성할_수_없다() {

        // time == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(name, date, null, theme)
        );
    }

    @Test
    void Reservation은_theme이_빈_채로_생성할_수_없다() {

        // theme == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(name, date, time, null)
        );
    }

    @Test
    void Reservation을_정상적으로_생성한_경우() {
        // given
        Reservation reservation = new Reservation(name, date, time, theme);

        // then
        assertThat(reservation.getName()).isEqualTo(name);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime()).isEqualTo(time);
        assertThat(reservation.getTheme()).isEqualTo(theme);
    }
}
