package roomescape.reservation.controller.dto;

import roomescape.reservation.Reservation;

public record WaitingResponse(
    Long id,
    String name,
    String theme,
    String date,
    String time,
    Long waitingNumber
) {

    public static WaitingResponse of(Reservation reservation, Long rank) {
        return new WaitingResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue(),
            rank
        );
    }
}
