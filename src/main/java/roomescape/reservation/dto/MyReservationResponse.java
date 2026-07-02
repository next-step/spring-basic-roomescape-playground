package roomescape.reservation.dto;

public record MyReservationResponse(
    Long reservationId,
    String theme,
    String date,
    String time,
    String status
) {
}
