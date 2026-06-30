package roomescape.member;

public class LoginMemberResponse {
    private String name;
    private String email;

    public LoginMemberResponse(String name, String email) {
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
