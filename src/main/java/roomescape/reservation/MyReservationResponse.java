package roomescape.reservation;

import java.time.LocalDate;
import roomescape.reservation.view.Formatter;

public record MyReservationResponse(long reservationId, String theme, LocalDate date
        , String time, String status) {

    public MyReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getThemeValue(),
                reservation.getDate(),
                reservation.getTimeValue().format(Formatter.TIME_FORMATTER),
                "예약"
        );
    }
}
