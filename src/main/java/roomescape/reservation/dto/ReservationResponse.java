package roomescape.reservation.dto;

import roomescape.reservation.entity.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String theme,
        String date,
        String time
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getTheme().getName(),
                reservation.getDate().toString(),
                reservation.getTime().getTimeValue()
        );
    }
}
