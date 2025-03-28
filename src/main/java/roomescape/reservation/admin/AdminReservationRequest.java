package roomescape.reservation.admin;

public class AdminReservationRequest {
    private String name;
    private String email;
    private String date;
    private Long theme;
    private Long time;

    public AdminReservationRequest(String name, String email, String date, Long theme, Long time) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public boolean hasEmail() {
        return !this.email.isBlank();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
}
