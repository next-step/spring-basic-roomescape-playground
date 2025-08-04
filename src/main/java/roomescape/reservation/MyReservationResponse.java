package roomescape.reservation;


import roomescape.waiting.WaitingResponse;

public class MyReservationResponse {

    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;

    private MyReservationResponse() {
    }

    private MyReservationResponse(Long id, String theme, String date, String time, String status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
            reservation.getId(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue(),
            "예약"
        );
    }

    public static MyReservationResponse from(WaitingResponse waitingResponse) {
        return new MyReservationResponse(
            waitingResponse.getId(),
            waitingResponse.getTheme(),
            waitingResponse.getDate(),
            waitingResponse.getTime(),
            waitingResponse.getWaitingNumber() + "번째 예약대기"
        );
    }

    public Long getId() {
        return id;
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
