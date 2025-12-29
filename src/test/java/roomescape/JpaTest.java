package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TimeDao.class)
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeDao timeDao;

    @Test
    void 사단계() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeDao.findAll().stream()
                                  .filter(t -> t.getId().equals(time.getId()))
                                  .findFirst()
                                  .orElse(null);

        assertThat(persistTime).isNotNull();
        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }
}
