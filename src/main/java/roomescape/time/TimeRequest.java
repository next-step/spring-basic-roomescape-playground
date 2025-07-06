package roomescape.time;

import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record TimeRequest(
        String value
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public TimeRequest {
        if (!StringUtils.hasText(value)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "시간값은 필수입니다");
        }
        try {
            LocalTime.parse(value, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "시간 형식이 올바르지 않습니다 (HH:mm)");
        }
    }

    public Time toEntity() {
        return new Time(value);
    }

}
