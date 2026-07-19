package roomescape.waiting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WaitingRequest(
        @NotBlank(message = "날짜는 필수입니다.")
        String date,

        @NotNull(message = "테마는 필수입니다.")
        Long theme,

        @NotNull(message = "시간은 필수입니다.")
        Long time
) {
}
