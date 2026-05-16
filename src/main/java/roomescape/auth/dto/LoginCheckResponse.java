package roomescape.auth.dto;

public class LoginCheckResponse {

    private String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
