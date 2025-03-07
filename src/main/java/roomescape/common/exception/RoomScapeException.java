package roomescape.common.exception;

public class RoomScapeException extends RuntimeException {

    private final int statusCode;

    public RoomScapeException(final int statusCode, final String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
