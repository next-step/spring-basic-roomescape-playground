package roomescape.domain.exception;

public class ThemeException extends IllegalArgumentException {

    public ThemeException(final String s) {
        super(s);
    }

    public ThemeException(final ExceptionCode code) {
        super(code.getMessage());
    }
}
