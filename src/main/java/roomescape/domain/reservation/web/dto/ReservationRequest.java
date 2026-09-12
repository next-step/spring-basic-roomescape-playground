package roomescape.domain.reservation.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationRequest (

        String name,

        @NotNull(message = "날짜 필드는 필수값입니다.")
        @FutureOrPresent(message = "날짜는 과거일 수 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "테마 필드는 필수값입니다.")
        Long theme,

        @NotNull(message = "시간 필드는 필수값입니다.")
        Long time
) {
}
