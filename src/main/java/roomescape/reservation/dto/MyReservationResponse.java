package roomescape.reservation.dto;

import roomescape.reservation.model.Reservation;
import roomescape.waiting.model.Waiting;
import roomescape.waiting.model.WaitingWithRank;

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
            reservation.getTime().getTime(),
            "예약"
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.getWaiting();
        Long rank = waitingWithRank.getRank();
        return new MyReservationResponse(
            waiting.getId(),
            waiting.getTheme().getName(),
            waiting.getDate(),
            waiting.getTime().getTime(),
            rank + "번째 예약대기"
        );
    }
}
