package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;

    protected Waiting(){

    }
    public Waiting(Member member, String date, Theme theme, Time time) {
        this.member = member;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public Long getId(){
        return id;
    }
    public Member getMember() {
        return member;
    }

    public String getDate() {
        return date;
    }

    public Theme getTheme() {
        return theme;
    }

    public Time getTime() {
        return time;
    }
}
