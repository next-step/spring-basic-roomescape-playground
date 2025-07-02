package roomescape.reservation;

import roomescape.waiting.WaitingResponse;

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
                "예약");
    }

    public static MyReservationResponse from(WaitingResponse waitingResponse) {
        return new MyReservationResponse(
                waitingResponse.id(),
                waitingResponse.theme(),
                waitingResponse.date(),
                waitingResponse.time(),
                waitingResponse.waitingNumber() + "번째 예약대기");
    }
}
