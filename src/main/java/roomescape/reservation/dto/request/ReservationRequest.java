package roomescape.reservation.dto.request;

import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

public class ReservationRequest {

    private final String name;
    private final String date;
    private final Long theme;
    private final Long time;

    public ReservationRequest(String name, String date, Long theme, Long time) {
        validateNotBlank(date, theme, time);
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    private void validateNotBlank(String date, Long theme, Long time) {
        if (date == null ) {
            throw new BadRequestException(ExceptionMessage.INVALID_DATE.getMessage());
        }
        if (theme == null) {
            throw new BadRequestException(ExceptionMessage.INVALID_THEME.getMessage());
        }
        if (time == null) {
            throw new BadRequestException(ExceptionMessage.INVALID_TIME.getMessage());
        }
    }

    public ReservationRequest createWith(String name) {
        return new ReservationRequest(name, this.date, this.theme, this.time);
    }

    public boolean isInvalidName() {
        return this.name == null || this.name.isBlank();
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
}
