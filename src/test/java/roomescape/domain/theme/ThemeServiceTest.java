package roomescape.domain.theme;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.global.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ThemeService.class)
public class ThemeServiceTest {

    private final String name = "theme_name";
    private final String description = "theme_description";

    @Autowired
    private ThemeService themeService;

    @Test
    void 이미_있는_테마_이름으로_saveTheme를_호출하면_예외가_발생한다() {
        // then
        Assertions.assertThrows(
                DataIntegrityViolationException.class,

                // when
                () -> themeService.saveTheme(1L, "dummy", description)
        );
    }

    @Test
    void 정상적으로_saveTheme_호출() {
        // when
        Theme theme = themeService.saveTheme(1L, name, description);

        // then
        assertThat(theme).isNotNull();
        assertThat(theme.getId()).isNotNull();
        assertThat(theme.getName()).isEqualTo(name);
        assertThat(theme.getDescription()).isEqualTo(description);
    }

    @Test
    void findAllTheme를_호출하면_저장된_모든_theme를_반환한다() {
        // given
        themeService.saveTheme(1L, name, description);

        // when
        List<Theme> allTheme = themeService.findAllTheme();

        // then
        assertThat(allTheme).hasSize(2);
    }

    @Test
    void 존재하지_않는_ID로_deleteTheme을_호출하면_예외를_던진다() {
        // then
        Assertions.assertThrows(
                NotFoundException.class,

                // when
                () -> themeService.deleteTheme(1L, -1L)
        );
    }

    @Test
    void deleteTheme을_호출하면_저장된_theme을_삭제한다() {
        // given
        Theme savedTheme = themeService.saveTheme(1L, name, description);

        // when
        themeService.deleteTheme(1L, savedTheme.getId());

        // then
        List<Theme> allTheme = themeService.findAllTheme();
        assertThat(allTheme).hasSize(1);
    }
}
