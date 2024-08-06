package roomescape.auth;

public record LoginResponse(
        String email,
        String role
) {
    public LoginResponse(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
