package roomescape.waiting.dto.request;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.waiting.domain.Waiting;

import java.time.LocalDate;

public record WaitingRequest(
        LocalDate date,
        long time,
        long theme
) {
    public Waiting toWaiting(long memberId, Time time, Theme theme) {
        return new Waiting(
                memberId,
                date,
                time,
                theme
        );
    }
}
