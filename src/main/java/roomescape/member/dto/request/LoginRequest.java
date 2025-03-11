package roomescape.member.dto.request;

public class LoginRequest {
    String email;
    String password;

    public LoginRequest(String email, String password) {
        validateEmail(email);
        validatePassword(password);
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email are required");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password are required");
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
