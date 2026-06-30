package roomescape.waiting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Member member;
    private String date;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    @Column(name = "\"order\"")
    private Long order;

    public Waiting(Member member, String date, Time time, Theme theme, Long order) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.order = order;
    }

    public Waiting() {
    }

    public void proceed() {
        order--;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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

    public Long getOrder() {
        return order;
    }
}
