package roomescape.reservation;

import roomescape.waiting.WaitingWithRank;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {
    public static MyReservationResponse ofReservation(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                "예약"
        );
    }

    public static MyReservationResponse ofWaiting(WaitingWithRank waiting) {
        return new MyReservationResponse(
                waiting.getWaiting().getId(),
                waiting.getWaiting().getTheme().getName(),
                waiting.getWaiting().getDate(),
                waiting.getWaiting().getTime().getValue(),
                (waiting.getRank() + 1) + "번째 예약대기"
        );
    }
}
