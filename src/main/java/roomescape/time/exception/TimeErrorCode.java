package roomescape.time.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum TimeErrorCode implements ErrorCode {

    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다.");

    private HttpStatus httpStatus;
    private String message;

    TimeErrorCode(HttpStatus httpStatus, String message) {
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
