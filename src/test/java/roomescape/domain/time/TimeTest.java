package roomescape.domain.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTest {

    private final LocalTime value = LocalTime.of(10, 0);

    @Test
    void Time은_value가_빈_채로_생성할_수_없다() {

        // value == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Time(null)
        );
    }

    @Test
    void Time을_정상적으로_생성한_경우() {
        // given
        Time time = new Time(value);

        // then
        assertThat(time.getValue()).isEqualTo(value);
    }
}
