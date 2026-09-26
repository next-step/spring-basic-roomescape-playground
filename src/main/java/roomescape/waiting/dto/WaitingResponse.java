package roomescape.waiting.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record WaitingResponse(
        Long id,
        String theme,
        LocalDate date,
        LocalTime time,
        Long waitingNumber
) {
}
