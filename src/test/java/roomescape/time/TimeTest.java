package roomescape.time;

import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeTest {

    @Test
    void 시간이_비어있으면_예외가_발생한다() {
        // given
        String invalidTime = "";

        // when & then
        assertThatThrownBy(() -> new Time(invalidTime))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("시간은 비어있을 수 없습니다.");
    }
}
