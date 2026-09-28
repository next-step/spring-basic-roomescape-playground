package roomescape.theme;

import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @Test
    void 테마_이름이_비어있으면_예외가_발생한다() {
        // given
        String invalidName = "";

        // when & then
        assertThatThrownBy(() -> new Theme(invalidName, "테마 설명입니다."))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("테마 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 테마_설명이_비어있으면_예외가_발생한다() {
        // given
        String invalidDescription = "";

        // when & then
        assertThatThrownBy(() -> new Theme("테마 이름", invalidDescription))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("테마 설명은 비어있을 수 없습니다.");
    }
}
