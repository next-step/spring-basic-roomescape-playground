package roomescape.domain.exception;

public class ReservationException extends IllegalArgumentException {

    public ReservationException(final String s) {
        super(s);
    }

    public ReservationException(final ExceptionCode code) {
        super(code.getMessage());
    }
}
