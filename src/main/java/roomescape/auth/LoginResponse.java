package roomescape.auth;

public class LoginResponse {
    private String name;

    public LoginResponse() {
    }

    public LoginResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
