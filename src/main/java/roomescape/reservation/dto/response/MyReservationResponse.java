package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.reservation.domain.Reservation;

public record MyReservationResponse(
    Long reservationId,
    String theme,
    LocalDate date,
    @JsonFormat(pattern = "HH:mm")
    LocalTime time,
    String status
) {
    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
            reservation.getId(),
            reservation.getTheme().getName(),
            LocalDate.parse(reservation.getDate()),
            LocalTime.parse(reservation.getTime().getTime()),
            "예약"
        );
    }
}
