package roomescape.reservation;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.ParticipationTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private ParticipationTime participationTime;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public Reservation(Long id, String name, String date, ParticipationTime participationTime, Theme theme, Member member) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.participationTime = participationTime;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(Long id, String name, String date, ParticipationTime participationTime, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.participationTime = participationTime;
        this.theme = theme;
    }

    public Reservation(String name, String date, ParticipationTime participationTime, Theme theme) {
        this.name = name;
        this.date = date;
        this.participationTime = participationTime;
        this.theme = theme;
    }

    public Reservation(String name, String date, ParticipationTime participationTime, Theme theme, Member member) {
        this.name = name;
        this.date = date;
        this.participationTime = participationTime;
        this.theme = theme;
        this.member = member;
    }

    public Reservation() {

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

    public ParticipationTime getTime() {
        return participationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
