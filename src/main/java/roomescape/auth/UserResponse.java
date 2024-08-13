package roomescape.auth;

public record UserResponse(
        String name,
        long id
) {
    public UserResponse(String name, long id) {
        this.name = name;
        this.id = id;

    }

    public String getName() {
        return name;

    }
    public long getId() {
        return id;

    }

}
