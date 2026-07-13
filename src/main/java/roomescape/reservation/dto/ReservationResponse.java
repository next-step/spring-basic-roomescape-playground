package roomescape.reservation.dto;

public record ReservationResponse(
        Long id,
        Long memberId,
        String name,
        String theme,
        String date,
        String time
) {
}