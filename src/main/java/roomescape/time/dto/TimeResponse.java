package roomescape.time.dto;

import roomescape.time.entity.Time;

public record TimeResponse(
        Long id,
        String value
) {

    public static TimeResponse from(Time time) {
        return new TimeResponse(
                time.getId(),
                time.getTimeValue()
        );
    }
}
