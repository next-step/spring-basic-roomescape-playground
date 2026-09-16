package roomescape.member;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
  @NotBlank(message = "이메일은 공백이거나, 누락될 수 없습니다.")
  private String email;

  @NotBlank(message = "비밀번호는 공백이거나, 누락될 수 없습니다.")
  private String password;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
