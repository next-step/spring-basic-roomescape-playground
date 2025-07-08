package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
public class Waiting {
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

    public Waiting(Long id, String name, String date, Member member, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public Waiting(String name, String date, Member member, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    protected Waiting() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Member getMember() { return member; }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

}
