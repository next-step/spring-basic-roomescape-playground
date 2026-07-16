package roomescape.reservation.dto;

import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationStatus;

public record MyReservationResponse(
        Long reservationId,
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
}
