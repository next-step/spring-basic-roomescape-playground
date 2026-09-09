package roomescape.time;

import jakarta.validation.constraints.NotBlank;

public record TimeRequest(
        @NotBlank(message = "값 필드는 비어 있을 수 없습니다.")
        String value
) {
}
