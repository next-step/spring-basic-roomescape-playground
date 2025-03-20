package roomescape.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends RoomScapeException {

    public UnAuthorizedException(String errorMessage) {
        super(HttpStatus.UNAUTHORIZED.value(), errorMessage);
    }
}
