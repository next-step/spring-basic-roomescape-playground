package roomescape.reservation.dto;

public class MyReservationResponse {
    private Long reservationId;
    private String theme;
    private String time;
    private String date;
    private String status;

    public MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getReservationId() {
        return  reservationId;
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
