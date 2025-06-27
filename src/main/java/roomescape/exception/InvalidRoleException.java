package roomescape.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException() {
        super("유효하지 않은 권한입니다");
    }
}
