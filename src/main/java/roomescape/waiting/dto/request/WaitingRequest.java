package roomescape.waiting.dto.request;

import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.waiting.domain.Waiting;

import java.time.LocalDate;

public record WaitingRequest(
        LocalDate date,
        long time,
        long theme
) {
    public WaitingRequest {
        validateDate(date);
        validateTheme(theme);
        validateTime(time);
    }

    private void validateDate(final LocalDate date) {
        if (date == null) {
            throw new BadRequestException(ExceptionMessage.INVALID_DATE.getMessage());
        }
    }

    private void validateTheme(final Long themeId) {
        if (themeId == null) {
            throw new BadRequestException(ExceptionMessage.INVALID_THEME.getMessage());
        }
    }

    private void validateTime(final Long timeId) {
        if (timeId == null) {
            throw new BadRequestException(ExceptionMessage.INVALID_TIME.getMessage());
        }
    }

    public Waiting toWaiting(long memberId, String name, Time time, Theme theme) {
        return new Waiting(memberId, name, date, time, theme);
    }
}
