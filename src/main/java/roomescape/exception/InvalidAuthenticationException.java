package roomescape.exception;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("인증 정보가 유효하지 않습니다.");
    }
}