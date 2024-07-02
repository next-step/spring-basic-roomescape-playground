package roomescape.reservation;

import java.util.List;
import java.util.stream.Collectors;

public class MyReservationResponse {

    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
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

    public static List<MyReservationResponse> from(List<ReservationResponse> reservationResponses) {
        return reservationResponses.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(), reservation.getTheme(), reservation.getDate(), reservation.getTime(), "예약"))
                .collect(Collectors.toList());
    }
}
