package roomescape.reservation;

public record ReservationCreateCommand(
        String name,
        String date,
        Long themeId,
        Long timeId
) {
}