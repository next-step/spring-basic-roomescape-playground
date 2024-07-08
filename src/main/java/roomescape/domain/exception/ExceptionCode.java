package roomescape.domain.exception;

public enum ExceptionCode {

    REQUEST_INVALID("요청 정보가 올바르지 않습니다."),
    RESULT_NOT_FOUND("조회 결과가 존재하지 않습니다.");

    private final String message;

    ExceptionCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
