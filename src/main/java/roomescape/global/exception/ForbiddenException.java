package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RoomescapeException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "해당 리소스에 대한 접근 권한이 없습니다.");
    }
}
