package roomescape.dto;

import java.time.LocalDate;
import roomescape.model.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String theme,
        LocalDate date,
        String time
) {
    public static ReservationResponse from(Reservation reservation) {
        String name = reservation.getName() != null ? reservation.getName() : reservation.getMember().getName();

        return new ReservationResponse(
                reservation.getId(),
                name,
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
}
