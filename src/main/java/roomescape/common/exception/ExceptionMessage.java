package roomescape.common.exception;

public enum ExceptionMessage {

    INVALID_EMAIL("잘못된 이메일 양식입니다."),
    INVALID_PASSWORD("잘못된 비밀번호 양식입니다."),
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
