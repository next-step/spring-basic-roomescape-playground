package roomescape.domain.exception;

public class LoginException extends IllegalArgumentException {

    public LoginException(final String s) {
        super(s);
    }

    public LoginException(final ExceptionCode code) {
        super(code.getMessage());
    }
}
