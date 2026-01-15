package roomescape.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        String name,
        @NotBlank String date,
        @NotNull Long theme,
        @NotNull Long time
) {
}
