package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public enum ThemeErrorCode implements ErrorCode {

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다.");

    private HttpStatus httpStatus;
    private String message;

    ThemeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
