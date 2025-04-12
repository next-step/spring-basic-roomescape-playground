package roomescape.member.dto;

public class CheckResponse {

    private final String name;

    public CheckResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
