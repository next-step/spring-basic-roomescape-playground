package roomescape.auth;

public record UserResponse(
        String name
) {
    public UserResponse(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

}
