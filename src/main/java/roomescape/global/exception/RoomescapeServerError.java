package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class RoomescapeServerError extends RoomescapeException {

    public RoomescapeServerError() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}
