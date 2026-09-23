package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.dto.WaitingWithRank;

public record MyReservationResponse(
        Long id,
        String theme,
        String date,
        String time,
        ReservationType type,
        Long waitingRank
) {
    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                ReservationType.RESERVATION,
                null
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.getWaiting();

        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                ReservationType.WAITING,
                waitingWithRank.getRank() + 1
        );
    }
}
