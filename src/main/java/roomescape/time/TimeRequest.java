package roomescape.time;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record TimeRequest(@NotBlank(message = "시간 값은 필수입니다.") LocalTime value) {
}
