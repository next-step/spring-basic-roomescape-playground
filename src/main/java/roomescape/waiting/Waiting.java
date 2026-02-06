package roomescape.waiting;

import jakarta.persistence.*;
import lombok.Getter;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Getter
@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Waiting() {
    }

    public static Waiting memberWaiting(String date, Time time, Theme theme, Member member) {
        Waiting w = new Waiting();
        w.date = date;
        w.time = time;
        w.theme = theme;
        w.member = member;
        return w;
    }
}
