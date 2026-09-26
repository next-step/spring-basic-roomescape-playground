package roomescape.reservation.service;

import java.time.LocalDate;

public record ReservationCommand(
        Long memberId,
        String name,
        LocalDate date,
        Long themeId,
        Long timeId
) {
}
