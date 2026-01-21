package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class JpaTest {

    @Autowired
    private TimeRepository timeRepository;

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Test
    void 사단계() {
        Time time = new Time("10:00");
        Time savedTime = timeRepository.save(time);

        Time persistTime = timeRepository.findById(savedTime.getId()).orElse(null);

        assertThat(persistTime).isNotNull();
        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }

    @Test
    void 팔단계() {
        assertThat(secretKey).isNotBlank();
    }
}
