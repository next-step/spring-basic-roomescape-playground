package roomescape.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RoomScapeException {

    public BadRequestException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }
}
