package roomescape.domain.time;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record TimeResponse(
        Long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime value
) {
    public static TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getValue());
    }
}
