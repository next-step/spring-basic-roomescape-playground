package roomescape.time.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TimeRequest(
        @NotNull(message = "예약 시간은 비어 있을 수 없습니다.")
        LocalTime value
) {
}
