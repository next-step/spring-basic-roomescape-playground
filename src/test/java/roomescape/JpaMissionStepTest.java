package roomescape;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import roomescape.time.Time;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application.properties")

@DataJpaTest
public class JpaMissionStepTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private TimeRepository timeRepository;

	@Test
	void 사단계() {
		Time time = new Time("10:00");
		entityManager.persist(time);
		entityManager.flush();

		Time persistTime = timeRepository.findById(time.getId()).orElse(null);

		assertThat(persistTime.getTime()).isEqualTo(time.getTime());
	}
}
