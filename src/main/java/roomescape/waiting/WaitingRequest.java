package roomescape.waiting;

import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import java.time.LocalDate;

public record WaitingRequest(
        LocalDate date,
        Long theme,
        Long time
) {
    public WaitingRequest {
        if (date == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "날짜는 필수입니다");
        }
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다");
        }
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다");
        }
    }
}
