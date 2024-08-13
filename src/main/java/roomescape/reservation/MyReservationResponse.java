package roomescape.reservation;

public record MyReservationResponse(
        Long id,
        String theme,
        String date,
        String time,
        String status
) {
    public MyReservationResponse(Long id, String theme, String date, String time, String status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
