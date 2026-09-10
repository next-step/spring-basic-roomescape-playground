package roomescape.member;

public class CheckLoginResponse {
    private String name;

    public CheckLoginResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
