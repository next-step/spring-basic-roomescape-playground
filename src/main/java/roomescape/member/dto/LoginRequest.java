package roomescape.member.dto;

public record LoginRequest(String email, String password) {
    public LoginRequest {
        validateRequiredFields(email, password);
    }

    private void validateRequiredFields(String email, String password) {
        if (email == null || email.strip().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
        }

        if (password == null || password.strip().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
    }

}

