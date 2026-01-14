package roomescape.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        String name,

        @NotBlank(message = "date는 필수입니다.")
        String date,

        @NotNull(message = "theme는 필수입니다.")
        Long theme,

        @NotNull(message = "time은 필수입니다")
        Long time) {
}
