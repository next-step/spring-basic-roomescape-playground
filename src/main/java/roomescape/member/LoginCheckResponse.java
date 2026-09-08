package roomescape.member;

public class LoginCheckResponse {
    private final String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
