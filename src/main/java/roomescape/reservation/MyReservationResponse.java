package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonAlias;

public record MyReservationResponse(
        @JsonAlias("reservationId") Long id,
        String theme,
        String date,
        String time,
        String status
) {
    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
