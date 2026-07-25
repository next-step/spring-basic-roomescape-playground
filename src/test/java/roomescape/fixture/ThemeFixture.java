package roomescape.fixture;

import roomescape.theme.entity.Theme;

@SuppressWarnings("NonAsciiCharacters")
public class ThemeFixture {

    public static Theme 테마_테마1_생성() {
        return Theme.of("테마1", "테마1입니다.");
    }

    public static Theme 테마_테마2_생성() {
        return Theme.of("테마2", "테마2입니다.");
    }

    public static Theme 테마_테마3_생성() {
        return Theme.of("테마3", "테마3입니다.");
    }
}
