package roomescape.waiting.domain;

import jakarta.persistence.*;
import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_waiting_member_date_time_theme",
                columnNames = {"member_id", "date", "time_id", "theme_id"}
        )
)
@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private String date;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;

    public Waiting() {
    }

    public Waiting(Member member, Theme theme, String date, Time time) {
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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
}
