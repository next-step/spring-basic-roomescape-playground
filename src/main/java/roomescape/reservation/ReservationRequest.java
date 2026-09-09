package roomescape.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest (

        @NotBlank(message = "이름 필드는 필수값입니다.")
        String name,

        @NotBlank(message = "날짜 필드는 필수값입니다.")
        String date,

        @NotNull(message = "테마 필드는 필수값입니다.")
        Long theme,

        @NotNull(message = "시간 필드는 필수값입니다.")
        Long time
) {
}
