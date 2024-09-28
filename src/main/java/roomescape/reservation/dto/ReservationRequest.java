package roomescape.reservation.dto;

public record ReservationRequest(
    String name,
    String date,
    Long theme,
    Long time
) {
}
