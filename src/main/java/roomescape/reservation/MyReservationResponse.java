package roomescape.reservation;

import roomescape.waiting.WaitingWithRank;

public record MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTime(),
                "예약"
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getDate(),
                waitingWithRank.getWaiting().getTime().getTime(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }

    public Long getId() {
        return reservationId;
    }

    public String getStatus() {
        return status;
    }
}