package roomescape.reservation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "uk_reservation_schedule",
        columnNames = {"date", "time_id", "theme_id"}
))
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    @ManyToOne(optional = false)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    protected Reservation() {
    }

    public Reservation(Long id, Member member, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Member member, LocalDate date, Time time, Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(Long id, Long memberId, String memberName, LocalDate date, Time time, Theme theme) {
        this(id, new Member(memberId, memberName, null, null, null), date, time, theme);
    }

    public Reservation(Long memberId, String memberName, LocalDate date, Time time, Theme theme) {
        this(null, memberId, memberName, date, time, theme);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getMemberName() {
        return member.getName();
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
