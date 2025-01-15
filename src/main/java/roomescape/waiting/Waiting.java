package roomescape.waiting;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String date;

    @ManyToOne
    @JoinColumn(name = "time_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Time time;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Waiting(Member member, String name, String date, Time time, Theme theme) {
        this.member = member;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }
    public Waiting(String name, String date, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
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

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
