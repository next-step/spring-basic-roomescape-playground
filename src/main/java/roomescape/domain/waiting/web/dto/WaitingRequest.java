package roomescape.domain.waiting.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WaitingRequest(

        @JsonFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "날짜는 과거일 수 없습니다.")
        @NotNull(message = "날짜 필드는 필수값입니다.")
        LocalDate date,

        @NotNull(message = "테마 필드는 필수값입니다.")
        Long theme,

        @NotNull(message = "시각 필드는 필수값입니다.")
        Long time
) {
}
