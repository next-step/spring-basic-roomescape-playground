package roomescape.reservation.dto;

import roomescape.reservation.entity.Reservation;
import roomescape.waiting.dto.WaitingWithRank;
import roomescape.waiting.entity.Waiting;

public record MyReservationResponse(
        Long id,
        String theme,
        String date,
        String time,
        String status
) {

    public static MyReservationResponse fromReservation(Reservation reservation, String status) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate().toString(),
                reservation.getTime().getTimeValue(),
                status
        );
    }

    public static MyReservationResponse fromWaitingWithRank(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.waiting();
        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate().toString(),
                waiting.getTime().getTimeValue(),
                (waitingWithRank.rank() + 1) + "번째 예약대기"
        );
    }
}
