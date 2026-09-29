package roomescape.domain.waiting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.entity.Member;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;
import roomescape.domain.waiting.entity.ReserveWaiting;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ReserveWaitingTest {

    private Member alice = new Member("Alice", "alice@dummy.com", "dummy", "USER");
    private LocalDate date = LocalDate.now();
    private Time time = new Time(LocalTime.of(11, 30));
    private Theme theme = new Theme("Dummy", "It is dummy for Test");

    @Test
    void ReserveWaiting은_member가_빈_채로_생성할_수_없다() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ReserveWaiting(null, date, time, theme)
        );
    }

    @Test
    void ReserveWaiting은_date가_빈_채로_생성할_수_없다() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ReserveWaiting(alice, null, time, theme)
        );
    }

    @Test
    void ReserveWaiting은_time이_빈_채로_생성할_수_없다() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ReserveWaiting(alice, date, null, theme)
        );
    }

    @Test
    void ReserveWaiting은_theme가_빈_채로_생성할_수_없다() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ReserveWaiting(alice, date, time, null)
        );
    }

    @Test
    void 정상적으로_ReserveWaiting을_생성() {
        // when
        ReserveWaiting reserveWaiting = new ReserveWaiting(alice, date, time, theme);

        // then
        assertThat(reserveWaiting.getId()).isNull();
        assertThat(reserveWaiting.getMember()).isEqualTo(alice);
        assertThat(reserveWaiting.getDate()).isEqualTo(date);
        assertThat(reserveWaiting.getTime()).isEqualTo(time);
        assertThat(reserveWaiting.getTheme()).isEqualTo(theme);
    }
}
