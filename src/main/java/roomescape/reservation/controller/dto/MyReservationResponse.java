package roomescape.reservation.controller.dto;

import roomescape.reservation.Reservation;
import roomescape.reservation.WaitingWithRank;

public record MyReservationResponse(
    Long id,
    String theme,
    String date,
    String time,
    String status
) {

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        Reservation reservation = waitingWithRank.reservation();
        Long rank = waitingWithRank.rank();

        return new MyReservationResponse(
            reservation.getId(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue(),
            rank == 0 ? "예약" : rank + "번째 예약대기"
        );
    }
}
