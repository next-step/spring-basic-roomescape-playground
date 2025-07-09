package roomescape.time;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.RoomEscapeException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TimeServiceTest {

    @Autowired
    TimeService timeService;

    @Autowired
    EntityManager entityManager;

    @Test
    void 시간이_정상적으로_생성된다() {
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

    @Test
    void 중복된_시간이_들어가면_안된다() {
        //given
        TimeRequest timeRequest1 = new TimeRequest("13:00");
        TimeRequest timeRequest2 = new TimeRequest("13:00");
        timeService.save(timeRequest1);

        //then
        assertThatThrownBy(() -> timeService.save(timeRequest2))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 존재하는 시간입니다.");
    }

    @Test
    void 소프트_삭제후_다시넣기_가능해야한다() {
        //given
        TimeRequest timeRequest1 = new TimeRequest("13:00");
        TimeRequest timeRequest2 = new TimeRequest("13:00");
        TimeResponse saved1 = timeService.save(timeRequest1);
        timeService.deleteById(saved1.id());
        entityManager.flush();

        //when
        TimeResponse saved2 = timeService.save(timeRequest2);

        //then
        assertThat(saved2.id()).isGreaterThan(0L);
        assertThat(saved2.value()).isEqualTo(timeRequest2.value());
    }

}
