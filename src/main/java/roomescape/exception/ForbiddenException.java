package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RoomScapeException {

    public ForbiddenException(String errorMessage) {
        super(HttpStatus.FORBIDDEN.value(), errorMessage);
    }
}
