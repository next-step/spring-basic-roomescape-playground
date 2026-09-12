package roomescape.global.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class RoomescapeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Long userId;
    private final Map<String, Object> rejectedValues;

    protected RoomescapeException(HttpStatus httpStatus, Long userId, Map<String, Object> rejectedValues, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.userId = userId;
        this.rejectedValues = rejectedValues;
    }

    protected RoomescapeException(HttpStatus httpStatus, String message) {
        this(httpStatus, null, Map.of(), message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public Map<String, Object> getRejectedValues() {
        return rejectedValues;
    }
}
