package roomescape.domain.theme;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThemeTest {

    private final String name = "테마";
    private final String description = "설명";

    @Test
    void Theme는_name이_빈_채로_생성할_수_없다() {

        // name == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Theme(null, description)
        );

        // name == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Theme("", description)
        );

        // name == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Theme(" ", description)
        );
    }

    @Test
    void Theme는_description이_빈_채로_생성할_수_없다() {

        // description == null
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Theme(name, null)
        );

        // description == ""
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Theme(name, "")
        );

        // description == " "
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Theme(name, " ")
        );
    }

    @Test
    void Theme를_정상적으로_생성한_경우() {
        // given
        Theme theme = new Theme(name, description);

        // then
        assertThat(theme.getName()).isEqualTo(name);
        assertThat(theme.getDescription()).isEqualTo(description);
    }
}
