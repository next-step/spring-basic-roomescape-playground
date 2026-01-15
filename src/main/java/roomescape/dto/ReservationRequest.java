package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(
        String name,
        Long memberId,
        LocalDate date,
        Long theme,
        Long time
) { }
