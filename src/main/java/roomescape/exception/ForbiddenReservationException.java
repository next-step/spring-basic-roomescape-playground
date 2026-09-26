package roomescape.exception;

public class ForbiddenReservationException extends RuntimeException {
    public ForbiddenReservationException(String message) {
        super(message);
    }
}
