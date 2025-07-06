package roomescape.time;

import io.restassured.internal.common.assertion.Assertion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.exception.RoomEscapeException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TimeServiceTest {

    @Autowired
    TimeService timeService;

    @Test
    void 시간이_정사적으로_생성된다() {
        //given
        TimeRequest timeRequest = new TimeRequest("11:11");

        //when
        TimeResponse saved = timeService.save(timeRequest);

        //then
        assertThat(saved.id()).isGreaterThan(0L);
        assertThat(saved.value()).isEqualTo(timeRequest.value());
    }

    @Test
    void 참조중인_시간_삭제하면_예외를_던진다() {
        //given
        List<TimeResponse> all = timeService.findAll();
        TimeResponse response = all.get(0);

        //then
        assertThatThrownBy(() -> timeService.deleteById(response.id()))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간이 다른 자원에서 사용중입니다.");
    }

}
