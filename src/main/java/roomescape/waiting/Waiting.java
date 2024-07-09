package roomescape.waiting;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private String date;

    public Waiting() {

    }

    public Waiting(Member member, Time time, Theme theme, String date) {
        this.member = member;
        this.time = time;
        this.theme = theme;
        this.date = date;
    }

    public Waiting(Long id, Member member, Time time, Theme theme, String date) {
        this.id = id;
        this.member = member;
        this.time = time;
        this.theme = theme;
        this.date = date;
    }

    public Long getId() {
        return id;
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

    public String getDate() {
        return date;
    }
}
