package roomescape.reservation;

public class MyReservationResponse {

    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long reservationId, String theme, String date, String time) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = "예약";
    }

    public Long getReservationId() { return reservationId; }
    public String getTheme()         { return theme; }
    public String getDate()          { return date; }
    public String getTime()          { return time; }
    public String getStatus()        { return status; }
}
