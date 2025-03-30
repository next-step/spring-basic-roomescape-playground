package roomescape.reservation.admin;

public class AdminReservationResponse {
    private Long id;
    private String name;
    private String email;
    private String theme;
    private String date;
    private String time;
    private String status;

    public AdminReservationResponse(Long id, String name, String email, String theme, String date, String time,
                                    String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
