package roomescape.reservation;

import jakarta.persistence.*;
import roomescape.member.model.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.Optional;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    @ManyToOne
    private Member member;

    public Reservation(Long id, String name, String date, Time time, Theme theme,Member member) {
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
        this.member = member;
    }

    public Reservation() {

    }

    public Reservation(String admin, String date, Time time, Theme theme, Member member) {
        name =admin;
        this.date = date;
        this.time =time;
        this.theme=theme;
        this.member=member;
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
