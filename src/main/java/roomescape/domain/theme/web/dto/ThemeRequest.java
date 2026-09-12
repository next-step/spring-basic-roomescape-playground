package roomescape.domain.theme.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(
        @NotBlank(message = "이름 필드는 필수값입니다.")
        String name,

        @NotBlank(message = "설명 필드는 필수값입니다.")
        String description
) {
}
