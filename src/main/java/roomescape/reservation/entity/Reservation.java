package roomescape.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import roomescape.member.entity.Member;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;

import java.time.LocalDate;

@Entity(name = "reservation")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_reservation_date_time_theme",
                columnNames = {"date", "time_id", "theme_id"}
        )
})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    public Reservation() {
    }

    private Reservation(Member member, LocalDate date, Time time, Theme theme) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(Member member, LocalDate date, Time time, Theme theme) {
        return new Reservation(
                member,
                date,
                time,
                theme
        );
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
