package roomescape.time;

import jakarta.validation.constraints.NotBlank;

public record TimeRequest(@NotBlank(message = "시간 값은 필수입니다.") String timeValue) {
}
