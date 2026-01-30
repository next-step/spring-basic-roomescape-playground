package roomescape.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FailMessage {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 40000, "잘못된 요청입니다."),

    AUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 40101, "이메일 또는 비밀번호가 틀렸습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40102, "유효하지 않은 토큰입니다."),

    FORBIDDEN_OWNERSHIP(HttpStatus.FORBIDDEN, 40301, "본인이 소유한 데이터만 삭제할 수 있습니다."),

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, 40401, "존재하지 않는 회원입니다."),
    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, 40402, "예약이 존재하지 않습니다."),
    NOT_FOUND_TIME(HttpStatus.NOT_FOUND, 40403, "존재하지 않는 시간입니다."),
    NOT_FOUND_THEME(HttpStatus.NOT_FOUND, 40404, "존재하지 않는 테마입니다."),

    CONFLICT_ALREADY_RESERVED(HttpStatus.CONFLICT, 40901, "이미 예약된 시간입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
