package roomescape.reservation;

import roomescape.waiting.WaitingResponse;

import java.time.LocalDate;

public record MyReservationResponse(
        Long id,
        String theme,
        LocalDate date,
        String time,
        String status
) {

    private static final String STATUS_RESERVED = "예약";
    private static final String STATUS_WAITING = "번째 예약대기";

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                STATUS_RESERVED);
    }

    public static MyReservationResponse from(WaitingResponse waitingResponse) {
        return new MyReservationResponse(
                waitingResponse.id(),
                waitingResponse.theme(),
                waitingResponse.date(),
                waitingResponse.time(),
                waitingResponse.waitingNumber() + STATUS_WAITING);
    }
}
