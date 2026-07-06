package roomescape.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "비밀번호는 필수 입력값이에요.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력값이에요.")
    @Email
    private String email;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
