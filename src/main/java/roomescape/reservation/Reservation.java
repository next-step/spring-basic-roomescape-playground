package roomescape.reservation;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    private Reservation(String name, String date, Member member, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    protected Reservation() {
    }

    public static Reservation of(String name, String date, Member member, Time time, Theme theme) {
        return new Reservation(name, date, member, time, theme);
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
