package roomescape.domain.time.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.time.entity.Time;

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
