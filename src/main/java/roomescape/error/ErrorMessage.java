package roomescape.error;

public enum ErrorMessage {
    MEMBER_NOT_FOUND("해당 멤버가 없습니다."),
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다."),
    TIME_NOT_FOUND("없는 시간입니다."),
    THEME_NOT_FOUND("없는 테마입니다."),
    WAITING_NOT_FOUND("없는 대기 명단입니다."),
    FORBIDDEN_RESERVATION("본인 이름으로만 예약할 수 있습니다."),
    FORBIDDEN_DELETE("본인 예약만 삭제할 수 있습니다."),
    NO_COOKIES_FOUND("쿠키가 없습니다. 로그인 상태를 확인해주세요."),
    NO_AUTH_TOKEN_FOUND("로그인 토큰이 없습니다. 쿠키를 확인해주세요."),
    ALREADY_WAITING("이미 대기 중인 시간입니다."),
    NOT_HAVE_DELETE_PERMISSION("삭제할 권한이 없습니다."),
    WAITING_MUST_AFTER_NOW("예약 대기는 현재 시간 이후로 해주시길 바랍니다."),
    RESERVATION_MUST_AFTER_NOW("예약은 현재 시간 이후로 해주시길 바랍니다.");

    private static final String PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
