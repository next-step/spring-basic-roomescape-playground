package roomescape.reservation;

import roomescape.theme.Theme;
import roomescape.time.Time;

public class ReservationResponse {
    private Long id;
    private String name;
    private Theme theme;
    private String date;
    private Time time;

    public ReservationResponse(Long id, String name, Theme theme, String date, Time time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Theme getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
}
