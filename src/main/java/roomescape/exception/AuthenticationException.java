package roomescape.exception;

public class AuthenticationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthenticationException() {
        this(ErrorCode.LOGIN_REQUIRED);
    }

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
