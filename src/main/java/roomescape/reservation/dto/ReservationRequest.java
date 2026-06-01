package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        String name,
        @NotNull String date,
        @NotNull Long themeId,
        @NotNull Long timeId
) {
}