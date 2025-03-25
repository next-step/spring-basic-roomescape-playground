package roomescape.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record ReservationResponse(Long id,
                                  String name,
                                  String theme,
                                  LocalDate date,
                                  String time) {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getThemeValue(),
                reservation.getDate(), reservation.getTimeValue().format(TIME_FORMATTER));
    }
}
