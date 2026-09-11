package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ReservationRequest(
        String name,
        LocalDate date,
        @JsonProperty("theme") Long themeId,
        @JsonProperty("time") Long timeId
) {
}
