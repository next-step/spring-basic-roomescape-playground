package roomescape.reservation;

import jakarta.persistence.*;
import lombok.Getter;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private LocalDate date;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Reservation() {
    }

    public Reservation(Long id, LocalDate date, Time time, Theme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(LocalDate date, Time time, Theme theme, Member member) {
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
