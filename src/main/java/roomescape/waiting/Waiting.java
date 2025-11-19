package roomescape.waiting;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.ParticipationTime;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String date;
    @JoinColumn(name = "time_id")
    @ManyToOne
    private ParticipationTime time;
    @JoinColumn(name = "theme_id")
    @ManyToOne
    private Theme theme;

    public Waiting(Member member, String date, ParticipationTime time, Theme theme) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Waiting() {

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

    public ParticipationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
