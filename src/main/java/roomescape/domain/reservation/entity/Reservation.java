package roomescape.domain.reservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import roomescape.domain.member.entity.Member;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

@Entity
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    public Reservation() {
    }

    public Reservation(Long id, String name, String date, Time time, Theme theme, Member member) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(String name, String date, Time time, Theme theme, Member member) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
