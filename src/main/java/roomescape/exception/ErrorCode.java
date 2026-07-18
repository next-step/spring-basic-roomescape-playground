package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ADMIN_AUTHORIZATION_REQUIRED(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    IDEMPOTENCY_KEY_CONFLICT(HttpStatus.CONFLICT, "동일한 Idempotency-Key로 다른 요청을 처리할 수 없습니다."),
    CONFLICT_REQUEST(HttpStatus.CONFLICT, "이미 존재하거나 현재 상태와 충돌하는 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
