package roomescape.reservation;

import roomescape.waiting.Waiting;

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

    public static MyReservationResponse from(Waiting waiting, Long rank) {
        return new MyReservationResponse(
            waiting.getId(),
            waiting.getTheme().getName(),
            waiting.getDate(),
            waiting.getTime().getTime(),
            (rank + 1) + "번째 예약대기"
        );
    }
}
