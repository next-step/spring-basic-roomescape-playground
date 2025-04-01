package roomescape.reservation;

import java.time.LocalDate;
import roomescape.reservation.view.Formatter;

public record ReservationResponse(Long id, String name, String theme, LocalDate date, String time) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getThemeValue(),
                reservation.getDate(), reservation.getTimeValue().format(Formatter.TIME_FORMATTER));
    }
}
