package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("findAllByDeletedFalse : 삭제되지 않은 테마 목록 조회")
    @Test
    void given_new_Theme_save_when_findAll_then_return() {
        //given
        Theme theme1 = new Theme("테마1", "설명1");
        Theme theme2 = new Theme("테마2", "설명2");
        Theme deletedTheme = new Theme("삭제된 테마", "설명3");
        deletedTheme.markAsDeleted();

        themeRepository.save(theme1);
        themeRepository.save(theme2);
        themeRepository.save(deletedTheme);

        // when
        List<Theme> themes = themeRepository.findAllByDeletedFalse();

        // then
        assertThat(themes).containsExactly(theme1, theme2)
                .doesNotContain(deletedTheme);
    }

}
