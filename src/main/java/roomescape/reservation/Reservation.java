package roomescape.reservation;

import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date;

    @ManyToOne()
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne()
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    protected Reservation() {
    }

    public Reservation(String name, Member member, String date, Time time, Theme theme) {
        this.name = name;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name != null ? name : member.getName();    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {return member;}
}
