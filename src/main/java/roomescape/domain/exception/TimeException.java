package roomescape.domain.exception;

public class TimeException extends IllegalArgumentException {

    public TimeException(final String s) {
        super(s);
    }

    public TimeException(final ExceptionCode code) {
        super(code.getMessage());
    }
}
