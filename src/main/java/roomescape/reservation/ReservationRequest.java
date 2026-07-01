package roomescape.reservation;

public record ReservationRequest(
        String name,
        String date,
        Long themeId,
        Long timeId
) {}