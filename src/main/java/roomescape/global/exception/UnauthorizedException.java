package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RoomescapeException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "로그인 정보를 찾을 수 없습니다.");
    }
}
