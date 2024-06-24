package roomescape.member;

public class TokenResponse {
    private String name;
    private String email;

    public TokenResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
