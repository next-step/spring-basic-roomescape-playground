package roomescape.domain.time;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TimeRequest(

        @JsonFormat(pattern = "HH:mm")
        @NotNull(message = "값 필드는 비어 있을 수 없습니다.")
        LocalTime value
) {
}
