package roomescape.exception;

public enum ExceptionMessage {

    INVALID_EMAIL("잘못된 이메일 양식입니다."),
    INVALID_PASSWORD("잘못된 비밀번호 양식입니다."),
    INVALID_DATE("잘못된 날짜 양식입니다."),
    INVALID_THEME("잘못된 테마 양식입니다."),
    INVALID_TIME("잘못된 시간 양식입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    INVALID_COOKIE_VALUE("잘못된 쿠키값입니다."),
    INVALID_SECRET_KEY("요청하신 키 값이 안전하지 않습니다. 길이가 긴 키 값을 제공해 주세요."),
    WAITING_ALREADY_EXISTS("이미 예약 대기 상태입니다."),
    RESERVATION_ALREADY_EXISTS("이미 존재하는 예약입니다."),
    RESERVATION_NOT_FOUND("해당 예약이 존재하지 않아 대기 없이 예약 가능합니다."),

    AUTHENTICATION_NEEDED("로그인이 필요합니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),

    UNAUTHORIZED_MEMBER("접근 권한이 없는 사용자입니다.");

    private static final String ERROR_PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(String message) {
        this.message = ERROR_PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
