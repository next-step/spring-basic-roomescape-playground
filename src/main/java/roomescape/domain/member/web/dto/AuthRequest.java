package roomescape.domain.member.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "email은 필수값입니다.")
        String email,

        @NotBlank(message = "password는 필수값입니다")
        String password
) {
}
