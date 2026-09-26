package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResult(
        Long id,
        String name,
        String theme,
        LocalDate date,
        LocalTime time
) {
}
