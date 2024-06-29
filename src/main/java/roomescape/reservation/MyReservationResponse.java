package roomescape.reservation;

public class MyReservationResponse {
    private Long reservationId;
    private String theme;
    private String time;
    private String date;
    private String status;

    public MyReservationResponse() {
    }

    public MyReservationResponse(Long reservationId, String theme, String time, String date, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.time = time;
        this.date = date;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getTheme() {
        return theme;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
