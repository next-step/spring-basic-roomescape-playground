package roomescape.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(Long id, String name, String theme, LocalDate date, LocalTime time) {

    public String getName() {
        return name;
    }
}
