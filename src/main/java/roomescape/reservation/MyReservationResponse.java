package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MyReservationResponse(
        @JsonProperty("id")
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {}
