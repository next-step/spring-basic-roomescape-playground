package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        String name,
        @NotNull String date,
        @NotNull String themeName,
        String themeDescription,
        @NotNull String time
) {
}