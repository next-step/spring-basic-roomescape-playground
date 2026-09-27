package roomescape.waiting;

public class DuplicatedReservationException extends RuntimeException {
    public DuplicatedReservationException(String message) {
        super(message);
    }
}
