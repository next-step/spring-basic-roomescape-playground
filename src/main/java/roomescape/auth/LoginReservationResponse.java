package roomescape.auth;

public record LoginReservationResponse(
        String name
) {
    public LoginReservationResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

