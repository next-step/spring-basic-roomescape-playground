package roomescape.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private Reservation(Member member, String name, LocalDate date, Time time, Theme theme) {
        validateNotPast(date);
        this.member = member;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = ReservationStatus.RESERVED;
    }

    public Reservation() {

    }

    public static Reservation forMember(Member member, LocalDate date, Time time, Theme theme) {
        return new Reservation(member, "", date, time, theme);
    }

    public static Reservation forGuest(String name, LocalDate date, Time time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
        return new Reservation(null, name, date, time, theme);
    }

    private void validateNotPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getReserverName() {
        if (member != null) {
            return member.getName();
        }
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Member getMember() {
        return member;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
