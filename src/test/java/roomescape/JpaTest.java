package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.time.Time;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void 사단계() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = entityManager.find(Time.class, time.getId());

        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }
}
