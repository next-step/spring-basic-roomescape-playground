package roomescape.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "로그인 email은 비어있을 수 없습니다.")
        String email,

        @NotBlank(message = "로그인 password는 비어있을 수 없습니다.")
        String password
) {
}
