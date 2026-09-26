package roomescape.waiting.service;

import java.time.LocalDate;
import java.time.LocalTime;

public record WaitingResult(
        Long id,
        String theme,
        LocalDate date,
        LocalTime time,
        Long waitingNumber
) {
}
