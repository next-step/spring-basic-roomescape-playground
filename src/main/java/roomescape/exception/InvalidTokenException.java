package roomescape.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("토큰이 유효하지 않습니다");
    }
}
