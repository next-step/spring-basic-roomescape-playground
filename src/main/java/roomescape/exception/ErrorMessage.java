package roomescape.exception;

public enum ErrorMessage {
    TIME_NOT_FOUND("해당 시간을 찾을 수 없습니다."),
    THEME_NOT_FOUND("해당 테마를 찾을 수 없습니다."),
    WAITING_NOT_FOUND("해당 대기를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND_BY_ID("ID %d에 해당하는 회원이 존재하지 않습니다."),
    MEMBER_NOT_FOUND_BY_NAME("이름이 '%s'인 회원이 존재하지 않습니다."),
    INVALID_LOGIN_CREDENTIALS("이메일 또는 비밀번호가 일치하지 않습니다."),
    LOGIN_REQUIRED("로그인이 필요합니다."),
    INVALID_AUTH_INFO("유효하지 않은 인증 정보입니다."),

    RESERVATION_TIME_ALREADY_BOOKED("해당 시간은 이미 예약이 완료되었습니다."),
    RESERVATION_ALREADY_EXISTS("이미 해당 시간에 예약이 존재합니다."),
    WAITING_ALREADY_EXISTS("이미 해당 시간에 예약 대기가 존재합니다."),
    MEMBER_INFO_REQUIRED("예약자 정보가 필요합니다."),
    TIME_VALUE_REQUIRED("시간 값은 필수입니다."),

    ONLY_OWN_WAITING_CAN_BE_CANCELLED("본인의 대기만 취소할 수 있습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
