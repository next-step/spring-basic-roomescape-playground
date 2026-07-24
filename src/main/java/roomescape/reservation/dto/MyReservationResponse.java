package roomescape.reservation.dto;

import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationStatus;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingWithRank;

public record MyReservationResponse(
        Long id,
        String theme,
        String date,
        String time,
        String status
) {

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                ReservationStatus.RESERVED.getValue()
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.getWaiting();

        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }
}
