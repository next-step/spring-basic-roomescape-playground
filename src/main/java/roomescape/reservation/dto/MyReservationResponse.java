package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        LocalDate date,
        LocalTime time,
        String status
) {
    @JsonIgnore
    public Long getId() {
        return reservationId;
    }

    public String getStatus() {
        return status;
    }
}
