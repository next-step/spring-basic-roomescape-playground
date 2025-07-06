package roomescape.theme;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.exception.RoomEscapeException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ThemeServiceTest {

    @Autowired
    ThemeService themeService;

    @Test
    void 테마가_정상_생성된다() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("테마1", "테마내용");

        //when
        ThemeResponse save = themeService.save(themeRequest);

        //then
        assertThat(save.id()).isGreaterThan(0L);
        assertThat(save.name()).isEqualTo(themeRequest.name());
        assertThat(save.description()).isEqualTo(themeRequest.description());
    }

    @Test
    void 참조되고있는_테마를_삭제하면_예외를_던진다() {
        //given
        List<ThemeResponse> all = themeService.findAll();
        ThemeResponse response = all.get(0);

        //when
        assertThatThrownBy(() -> themeService.deleteById(response.id()))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("테마가 다른 자원에서 사용중입니다.");
    }

    @Test
    void 유요하지_않은_테마를_삭제하면_예외를_던진다() {

        //when
        assertThatThrownBy(() -> themeService.deleteById(999L))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("해당 테마 정보를 찾을 수 없습니다.");
    }
}
