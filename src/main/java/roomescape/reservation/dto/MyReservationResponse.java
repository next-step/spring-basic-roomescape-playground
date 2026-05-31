package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Waiting;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {

    public static MyReservationResponse ofReservation(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(), reservation.getTheme().getName(), reservation.getDate(),
                reservation.getTime().getValue(), "예약");
    }

    public static MyReservationResponse ofWaiting(Waiting waiting, long rank) {
        return new MyReservationResponse(
                waiting.getId(), waiting.getTheme().getName(), waiting.getDate(),
                waiting.getTime().getValue(),
                rank + "번째 예약대기");
    }
}
