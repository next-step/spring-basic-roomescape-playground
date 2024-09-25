package roomescape.member;

public record MyReservationResponse(
    Long reservationId,
    String theme,
    String date,
    String time,
    String status
) {
}
