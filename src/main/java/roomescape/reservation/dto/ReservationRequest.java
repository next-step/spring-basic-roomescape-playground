package roomescape.reservation.dto;

public record ReservationRequest(
        String name,
        String date,
        Long themeId,
        Long timeId
) {}
