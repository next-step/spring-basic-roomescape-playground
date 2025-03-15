package roomescape.exception;

public enum ExceptionMessage {

    INVALID_EMAIL("잘못된 이메일 양식입니다."),
    INVALID_PASSWORD("잘못된 비밀번호 양식입니다."),
    INVALID_DATE("잘못된 날짜 양식입니다."),
    INVALID_THEME("잘못된 테마 양식입니다."),
    INVALID_TIME("잘못된 시간 양식입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    COOKIE_NOT_FOUND("존재하지 않는 쿠키입니다."),
    INVALID_COOKIE_VALUE("잘못된 쿠키값입니다."),

    UNAUTHORIZED_MEMBER("접근 권한이 없는 사용자입니다."),
    ;

    private static final String ERROR_PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(String message) {
        this.message = ERROR_PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
