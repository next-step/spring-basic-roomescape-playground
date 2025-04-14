package roomescape.reservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(long id, String timeValue) {

    public ReservationTimeResponse(ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getTimeValue().toString());
    }
}
