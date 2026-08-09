package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "email 또는 비밀번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(HttpStatus httpStatus, String message) {
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
