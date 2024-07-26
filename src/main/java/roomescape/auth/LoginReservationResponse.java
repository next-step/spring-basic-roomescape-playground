package roomescape.auth;

public class LoginReservationResponse {
    private String name;

    public LoginReservationResponse() {
    }

    public LoginReservationResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

