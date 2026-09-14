package roomescape.global.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BadRequestException extends RoomescapeException {
    public BadRequestException(Long userId, Map<String, Object> rejectedValues, String message) {
        super(HttpStatus.BAD_REQUEST, userId, rejectedValues, message);
    }
}
