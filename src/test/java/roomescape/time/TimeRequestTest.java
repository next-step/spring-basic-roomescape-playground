package roomescape.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.exception.RoomEscapeException;

class TimeRequestTest {

    @Test
    void 시간_형식에_맞지않으면_예외를_던진다() {

        Assertions.assertThatThrownBy(() -> new TimeRequest("11:ddd"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간 형식이 올바르지 않습니다 (HH:mm)");

        Assertions.assertThatThrownBy(() -> new TimeRequest(" "))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간값은 필수입니다");

        Assertions.assertThatThrownBy(() -> new TimeRequest("11:3"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간 형식이 올바르지 않습니다 (HH:mm)");

        Assertions.assertThatThrownBy(() -> new TimeRequest("11 3"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간 형식이 올바르지 않습니다 (HH:mm)");
    }
}
