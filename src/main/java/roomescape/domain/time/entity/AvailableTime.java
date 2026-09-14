package roomescape.domain.time.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class AvailableTime {
    private Long timeId;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private boolean booked;

    public AvailableTime(Long timeId, LocalTime time, boolean booked) {
        this.timeId = timeId;
        this.time = time;
        this.booked = booked;
    }

    public LocalTime getTime() {
        return time;
    }
}
