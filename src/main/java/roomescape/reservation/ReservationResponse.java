package roomescape.reservation;

public record ReservationResponse(
        String name,
        Long id,
        String theme,
        String date,
        String time) {
}
