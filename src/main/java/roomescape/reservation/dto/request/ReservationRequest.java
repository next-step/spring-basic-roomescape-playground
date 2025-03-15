package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

import java.time.LocalDate;

public class ReservationRequest {

    private final String name;
    private final LocalDate date;

    @JsonProperty("themeId")
    private final Long themeId;

    @JsonProperty("timeId")
    private final Long timeId;

    public ReservationRequest(String name, LocalDate date, Long themeId, Long timeId) {
        validateDate(date);
        validateTheme(themeId);
        validateTime(timeId);
        this.name = name;
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
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
        return new ReservationRequest(name, this.date, this.themeId, this.timeId);
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

    public Long getThemeId() {
        return themeId;
    }

    public Long getTimeId() {
        return timeId;
    }
}
