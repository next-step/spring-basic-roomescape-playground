package roomescape.exception;

public class ErrorResponse {
    private final String message;
    private final String type;

    public ErrorResponse(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
