package roomescape.member;

public class LoginRequest {

    private String password;
    private String email;

    public LoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}