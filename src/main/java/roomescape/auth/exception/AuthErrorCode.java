package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNAUTHENTICATED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다.");

    private HttpStatus httpStatus;
    private String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
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
