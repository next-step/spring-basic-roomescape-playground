package roomescape.exception;

public abstract class RoomescapeException extends RuntimeException {
    public RoomescapeException(String message) {
        super(message);
    }
}
