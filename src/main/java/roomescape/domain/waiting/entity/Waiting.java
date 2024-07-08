package roomescape.domain.waiting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import roomescape.domain.member.entity.Member;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

@Entity
public class Waiting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @ManyToOne
    private Member member;
    @ManyToOne
    private Time time;
    @ManyToOne
    private Theme theme;

    public Waiting() {
    }

    public Waiting(final String date, final Member member, final Time time, final Theme theme) {
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
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
