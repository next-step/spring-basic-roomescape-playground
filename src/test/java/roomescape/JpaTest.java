package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@DataJpaTest
public class JpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("Time 레포지토리 테스트")
    @Test
    void time_repository_test() {
        // given
        Time time = new Time("10:00");

        // when
        entityManager.persist(time);
        entityManager.flush();
        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        // then
        assertThat(persistTime.getTime()).isEqualTo(time.getTime());
    }

    @DisplayName("Theme 레포지토리 테스트")
    @Test
    void theme_repository_test() {
        // given
        Theme theme = new Theme("Theme", "This is a Theme.");

        // when
        entityManager.persist(theme);
        entityManager.flush();
        Theme persistTheme = themeRepository.findById(theme.getId()).orElseThrow();

        // then
        assertThat(persistTheme.getName()).isEqualTo(theme.getName());
    }

}
