package roomescape.auth;

public record TokenRequest(
        String email,
        String password
) {
    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
}
