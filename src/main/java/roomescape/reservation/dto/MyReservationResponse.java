package roomescape.reservation.dto;

public record MyReservationResponse(
    Long id,
    String theme,
    String date,
    String time,
    String status
) {
}
