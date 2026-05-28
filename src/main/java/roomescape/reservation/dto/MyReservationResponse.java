package roomescape.reservation.dto;

import roomescape.reservation.domain.ReservationStatus;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        ReservationStatus status
) {

}
