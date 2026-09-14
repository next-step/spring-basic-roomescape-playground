package roomescape.global.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ConflictException extends RoomescapeException {
    public ConflictException(Long userId, Map<String, Object> rejectedInputs, String message) {
        super(HttpStatus.CONFLICT, userId, rejectedInputs, message);
    }
}
