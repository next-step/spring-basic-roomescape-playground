package roomescape.domain.theme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeDao;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({ThemeDao.class})
public class ThemeDaoTest {

    private final String name = "새테마";
    private final String description = "새테마입니다.";

    @Autowired
    private ThemeDao themeDao;

    @Test
    void save를_호출하면_ID가_있는_객체를_반환한다() {
        // given
        Theme theme = new Theme(name, description);

        // when
        Theme savedTheme = themeDao.save(theme);

        // then
        assertThat(theme.getId()).isNull();
        assertThat(savedTheme.getId()).isNotNull();

        assertThat(savedTheme.getName()).isEqualTo(name);
        assertThat(savedTheme.getDescription()).isEqualTo(description);
    }

    @Test
    void findAll을_호출하면_저장된_모든_테마를_반환한다() {
        // schema.sql 시드 테마 3건
        assertThat(themeDao.findAll()).hasSize(3);

        // when
        themeDao.save(new Theme(name, description));

        // then
        assertThat(themeDao.findAll()).hasSize(4);
    }

    @Test
    void deleteById를_호출하면_해당_테마가_조회에서_제외된다() {
        // when
        themeDao.deleteById(1L);

        // then
        assertThat(themeDao.findAll()).hasSize(2);
    }
}
