package roomescape.exception;

public class InvalidReservationException extends RuntimeException {
    private InvalidReservationException(String message) {
        super(message);
    }

    public static InvalidReservationException invalidTheme() {
        return new InvalidReservationException("Invalid theme");
    }

    public static InvalidReservationException invalidTime() {
        return new InvalidReservationException("Invalid time");
    }
}
