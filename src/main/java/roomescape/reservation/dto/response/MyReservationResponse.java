package roomescape.reservation.dto.response;

import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationResponse(
        long reservationId,
        String theme,
        LocalDate date,
        LocalTime time,
        String status
) {
    public MyReservationResponse(Reservation reservation) {
        this(reservation.getId(),
                reservation.getThemeName(),
                reservation.getDate(),
                reservation.getTimeValue(),
                "예약"
        );
    }
}
