package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class TimeRepositoryTest {

    @Autowired
    private TimeRepository timeRepository;

    @DisplayName("findAllByDeletedFalse : 새로 저장한 Time 모두 조회 시 포함한다.")
    @Test
    void given_new_timeEntity_when_findAll_then_contain_result() {
        //given
        Time time1 = new Time("10:00");
        Time time2 = new Time("11:00");
        Time time3 = new Time("12:00");
        time3.markAsDeleted();

        timeRepository.save(time1);
        timeRepository.save(time2);
        timeRepository.save(time3);

        // when
        List<Time> times = timeRepository.findAllByDeletedFalse();

        // then
        assertThat(times).containsExactly(time1, time2)
                .doesNotContain(time3);
    }

}
