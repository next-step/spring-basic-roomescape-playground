package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.member.Member;
import roomescape.reservation.Reservation;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    protected Waiting() {

    }

    public Waiting(Member member, Reservation reservation) {
        this.member = member;
        this.reservation = reservation;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public String getThemeValue() {
        return reservation.getThemeValue();
    }

    public LocalDate getDate() {
        return reservation.getDate();
    }

    public LocalTime getTime() {
        return reservation.getTimeValue();
    }

    public String getMemberName() {
        return member.getName();
    }

    public boolean isOwner(long id) {
        return this.member.getId()
                .equals(id);
    }
}
