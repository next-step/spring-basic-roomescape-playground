package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        String name,
        @NotNull String date,
        @NotNull Long theme,
        @NotNull Long time
) {
}