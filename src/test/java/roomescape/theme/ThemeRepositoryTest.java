package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("findAllByDeletedFalse : 삭제되지 않은 테마 목록 조회")
    @Test
    void given_new_Theme_save_when_findAll_then_return() {
        //given
        Theme theme = Theme.ofDeletedFalse("테마1", "설명1");
        themeRepository.save(theme);
        // when
        List<Theme> themes = themeRepository.findAllByDeletedFalse();
        // then
        assertThat(themes).contains(theme);
    }

    @DisplayName("findAllByDeletedFalse : 삭제된 테마는 조회되지 않음")
    @Test
    void given_deleted_Theme_save_when_findAll_then_return() {
        //given
        Theme theme = Theme.ofDeletedFalse("테마1", "설명1");
        themeRepository.save(theme);
        theme.martAsDeleted();
        // when
        List<Theme> themes = themeRepository.findAllByDeletedFalse();
        // then
        assertThat(themes).doesNotContain(theme);
    }
}
