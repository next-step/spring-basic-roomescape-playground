package roomescape.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.member.Member;
import roomescape.reservation.study.ForStudy;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;

@Entity
@NamedEntityGraph(name = "Reservation.reservationTime", attributeNodes = @NamedAttributeNode("reservationTime"))
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Reservation() {
    }

    public Reservation(long id, String name, LocalDate date, ReservationTime reservationTime
            , Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(Member member, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.member = member;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return reservationTime;
    }

    public LocalTime getTimeValue() {
        return reservationTime.getTimeValue();
    }

    public Theme getTheme() {
        return theme;
    }

    public String getThemeValue() {
        return theme.getName();
    }

    public Member getMember() {
        return member;
    }

    public boolean isOwner(Member member) {
        return this.member.getId()
                .equals(member.getId());
    }
}
