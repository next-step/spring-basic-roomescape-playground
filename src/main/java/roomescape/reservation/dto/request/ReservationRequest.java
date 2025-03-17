package roomescape.reservation.dto.request;

import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.time.LocalDate;

public class ReservationRequest {

    private final String name;
    private final LocalDate date;
    private final Long theme;
    private final Long time;

    public ReservationRequest(String name, LocalDate date, Long theme, Long time) {
        validateDate(date);
        validateTheme(theme);
        validateTime(time);
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
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

    public ReservationRequest createWith(String name) {
        return new ReservationRequest(name, this.date, this.theme, this.time);
    }

    public boolean isInvalidName() {
        return this.name == null || this.name.isBlank();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }

    public Reservation toReservation(Time time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }
}
