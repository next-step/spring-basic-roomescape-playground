package roomescape.reservation;

import roomescape.theme.Theme;
import roomescape.time.Time;

public record Reservation(Long id, String name, String date, Time time, Theme theme) {
    public Reservation(String name, String date, Time time, Theme theme) {
        this(null, name, date, time, theme);
    }
}
