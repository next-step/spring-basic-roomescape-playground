package roomescape.reservation;

import roomescape.waiting.WaitingWithRank;

public class MyReservationResponse {
    private static final String RESERVATION_STATUS = "예약";
    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long id, String theme, String date, String time, String status) {
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
                RESERVATION_STATUS
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getDate(),
                waitingWithRank.getWaiting().getTime().getValue(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }

    public Long getId() { return id; }
    public String getTheme() { return theme; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
}
