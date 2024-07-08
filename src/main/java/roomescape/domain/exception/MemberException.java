package roomescape.domain.exception;

public class MemberException extends IllegalArgumentException {

    public MemberException(final String s) {
        super(s);
    }

    public MemberException(final ExceptionCode code) {
        super(code.getMessage());
    }
}
