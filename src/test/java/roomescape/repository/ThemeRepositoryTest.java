package roomescape.repository;

import org.junit.jupiter.api.Test;
import roomescape.theme.entity.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.ThemeFixture.테마_테마1_생성;
import static roomescape.fixture.ThemeFixture.테마_테마2_생성;
import static roomescape.fixture.ThemeFixture.테마_테마3_생성;

@SuppressWarnings("NonAsciiCharacters")
public class ThemeRepositoryTest extends RepositoryTest {

    @Test
    void 삭제되지_않은_테마만_조회한다() {
        // given
        final Theme theme1 = 테마_테마1_생성();
        final Theme theme2 = 테마_테마2_생성();
        final Theme theme3 = 테마_테마3_생성();
        복수_테마_저장(theme1, theme2, theme3);

        theme1.markDeleted();

        // when
        List<Theme> nonDeletedThemes = themeRepository.findAllByDeletedAtNull();

        // then
        assertThat(nonDeletedThemes)
                .extracting(Theme::getId)
                .containsExactlyInAnyOrder(
                        theme2.getId(),
                        theme3.getId()
                );
    }
}
