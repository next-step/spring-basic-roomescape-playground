package roomescape.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest (
        @NotBlank(message = "이름 필드는 필수값입니다.")
        String name,

        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일 필드는 필수값입니다.")
        String email,

        @NotBlank(message = "비밀번호 필드는 필수값입니다.")
        String password
) {

}
