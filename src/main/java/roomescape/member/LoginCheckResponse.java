package roomescape.member;

public class LoginCheckResponse {

    private String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }

    // Getter 추가
    public String getName() {
        return name;
    }
}
