package roomescape.reservation;

import java.time.LocalDate;

public record MyReservationResponse(long reservationId, String theme, LocalDate date
        , String time, String status) {
}
