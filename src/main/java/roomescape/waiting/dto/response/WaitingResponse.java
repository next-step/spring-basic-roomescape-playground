package roomescape.waiting.dto.response;

import roomescape.waiting.domain.Waiting;

import java.time.LocalDate;
import java.time.LocalTime;

public record WaitingResponse(
        long id,
        long memberId,
        LocalDate date,
        LocalTime time,
        String theme
) {
    public WaitingResponse(Waiting waiting) {
        this(
                waiting.getId(),
                waiting.getMemberId(),
                waiting.getDate(),
                waiting.getTimeValue(),
                waiting.getThemeName()
        );
    }
}
