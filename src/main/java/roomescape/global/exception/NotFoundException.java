package roomescape.global.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class NotFoundException extends RoomescapeException {
    public NotFoundException(Long userId, Map<String, Object> rejectedValues, String message) {
        super(HttpStatus.NOT_FOUND, userId, rejectedValues, message);
    }
}
