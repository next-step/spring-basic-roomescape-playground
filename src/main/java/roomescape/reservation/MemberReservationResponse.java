package roomescape.reservation;

import java.time.LocalDate;
import roomescape.reservation.view.Formatter;

public record MemberReservationResponse(long reservationId, String theme, LocalDate date
        , String time, String status) {

    public MemberReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getThemeValue(),
                reservation.getDate(),
                reservation.getTimeValue().format(Formatter.TIME_FORMATTER),
                "예약"
        );
    }
}
