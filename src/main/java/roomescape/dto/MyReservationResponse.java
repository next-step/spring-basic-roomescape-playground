package roomescape.dto;

import java.time.LocalDate;

public record MyReservationResponse(
        Long id,
        String theme,
        LocalDate date,
        String time,
        String status
) { }
