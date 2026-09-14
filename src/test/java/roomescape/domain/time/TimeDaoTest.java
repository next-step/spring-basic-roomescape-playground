package roomescape.domain.time;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeDao;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({TimeDao.class})
public class TimeDaoTest {

    private final LocalTime value = LocalTime.of(11, 0);

    @Autowired
    private TimeDao timeDao;

    @Test
    void save를_호출하면_ID가_있는_객체를_반환한다() {
        // given
        Time time = new Time(value);

        // when
        Time savedTime = timeDao.save(time);

        // then
        assertThat(time.getId()).isNull();
        assertThat(savedTime.getId()).isNotNull();

        assertThat(savedTime.getValue()).isEqualTo(value);
    }

    @Test
    void findAll을_호출하면_저장된_모든_시간을_반환한다() {
        // schema.sql 시드 시간 6건
        assertThat(timeDao.findAll()).hasSize(6);

        // when
        timeDao.save(new Time(value));

        // then
        assertThat(timeDao.findAll()).hasSize(7);
    }

    @Test
    void deleteById를_호출하면_해당_시간이_조회에서_제외된다() {
        // when
        timeDao.deleteById(1L);

        // then
        assertThat(timeDao.findAll()).hasSize(5);
    }
}
