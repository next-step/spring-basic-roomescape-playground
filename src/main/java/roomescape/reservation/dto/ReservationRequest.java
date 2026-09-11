package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReservationRequest(
        String name,
        String date,
        @JsonProperty("theme") Long themeId,
        @JsonProperty("time") Long timeId
) {
}
