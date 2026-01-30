package roomescape.reservation;

import jakarta.persistence.*;
import lombok.Getter;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Getter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

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

    protected Reservation() {
    }

    public static Reservation adminReservation(String name, String date, Time time, Theme theme) {
        Reservation r = new Reservation();
        r.name = name;
        r.date = date;
        r.time = time;
        r.theme = theme;
        r.member = null;
        return r;
    }

    public static Reservation memberReservation(String date, Time time, Theme theme, Member member) {
        Reservation r = new Reservation();
        r.name = member.getName();
        r.date = date;
        r.time = time;
        r.theme = theme;
        r.member = member;

        return r;
    }
}
