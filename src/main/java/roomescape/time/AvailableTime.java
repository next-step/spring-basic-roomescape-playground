package roomescape.time;

import lombok.Getter;

public class AvailableTime {

    @Getter
    private Long timeId;
    @Getter
    private String time;
    @Getter
    private boolean booked;

    public AvailableTime(Long timeId, String time, boolean booked) {
        this.timeId = timeId;
        this.time = time;
        this.booked = booked;
    }

    public boolean isBooked() {
        return booked;
    }
}
