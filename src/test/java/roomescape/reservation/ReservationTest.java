package roomescape.reservation;

import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 예약자_이름이_비어있으면_예외가_발생한다() {
        // given
        String invalidName = "";

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, invalidName, "2026-03-25", null, null, null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("예약자 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 예약_날짜가_비어있으면_예외가_발생한다() {
        // given
        String invalidDate = "";

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, "홍길동", invalidDate, null, null, null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("예약 날짜는 비어있을 수 없습니다.");
    }
}
