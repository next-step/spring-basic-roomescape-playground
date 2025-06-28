package roomescape.reservation;

import lombok.Getter;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Getter
public class Reservation {
    private Long id;
    private String name;
    private String date;
    private Time time;
    private Theme theme;

    public Reservation(Long id, String name, String date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String date, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }
}
