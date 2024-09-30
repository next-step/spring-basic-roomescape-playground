package roomescape.time.exception;

import roomescape.global.exception.global.DataNotFoundException;

public class TimeNotFoundException extends DataNotFoundException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 시간대입니다.";

    protected TimeNotFoundException(String message) {
        super(message);
    }

    public static TimeNotFoundException withMessage() {
        return new TimeNotFoundException(DEFAULT_MESSAGE);
    }
}
