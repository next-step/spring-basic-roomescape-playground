package roomescape.waiting;

import java.time.LocalDate;

public class WaitingRequest {

    private final LocalDate date;
    private final Long theme;
    private final Long time;

    public WaitingRequest(LocalDate date, Long theme, Long time) {
        this.date = date;
        this.theme = theme;
        this.time = time;
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
}
