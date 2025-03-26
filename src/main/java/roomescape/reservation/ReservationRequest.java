package roomescape.reservation;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long theme, Long time) {
}
