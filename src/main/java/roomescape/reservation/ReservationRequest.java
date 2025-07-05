package roomescape.reservation;

import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

public record ReservationRequest(String name, String date, Long theme, Long time) {

    public ReservationRequest {
        if (!StringUtils.hasText(date)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "날짜는 필수입니다.");
        }
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마는 필수입니다.");
        }
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "시간은 필수입니다.");
        }
    }

    public ReservationRequest withDefaultName(String defaultName) {
        return new ReservationRequest(defaultName, date, theme, time);
    }
}
