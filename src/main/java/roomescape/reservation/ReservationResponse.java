package roomescape.reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        String theme,
        LocalDate date,
        String time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
}
