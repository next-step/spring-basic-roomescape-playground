package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(Long id, String name, String theme, LocalDate date, LocalTime time) {
}
