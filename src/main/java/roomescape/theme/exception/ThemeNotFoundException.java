package roomescape.theme.exception;

import roomescape.global.exception.global.DataNotFoundException;

public class ThemeNotFoundException extends DataNotFoundException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 테마입니다.";

    protected ThemeNotFoundException(String message) {
        super(message);
    }

    public static ThemeNotFoundException withMessage() {
        return new ThemeNotFoundException(DEFAULT_MESSAGE);
    }
}
