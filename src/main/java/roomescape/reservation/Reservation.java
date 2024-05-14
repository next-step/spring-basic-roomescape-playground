package roomescape.reservation;

import jakarta.persistence.*;
import roomescape.theme.Theme;
import roomescape.time.Time;
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String date;
    @ManyToOne
    private Time time;

    @ManyToOne
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

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
