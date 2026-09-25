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

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private Reservation(Member member, String name, String date, Time time, Theme theme) {
        this.member = member;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = ReservationStatus.RESERVED;
    }

    public Reservation() {

    }

    public static Reservation forMember(Member member, String date, Time time, Theme theme) {
        return new Reservation(member, "", date, time, theme);
    }

    public static Reservation forGuest(String name, String date, Time time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
        return new Reservation(null, name, date, time, theme);
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

    public String getDate() {
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
