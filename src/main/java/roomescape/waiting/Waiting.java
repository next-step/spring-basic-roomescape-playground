package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@Table(name = "waiting")
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Waiting() {
    }

    public Waiting(Theme theme, String date, Time time, Member member) {
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.member = member;
    }

    public boolean isNotSameMember(Long memberId) {
        return !this.member.getId().equals(memberId);
    }

    public Long getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Member getMember() {
        return member;
    }
}
