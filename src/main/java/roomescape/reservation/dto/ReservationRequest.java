package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationRequest(
        Long memberId,
        String name,
        @NotNull(message = "예약 날짜, 테마, 시간은 필수입니다.")
        LocalDate date,
        @JsonProperty("theme")
        @NotNull(message = "예약 날짜, 테마, 시간은 필수입니다.")
        Long themeId,
        @JsonProperty("time")
        @NotNull(message = "예약 날짜, 테마, 시간은 필수입니다.")
        Long timeId
) {
}
