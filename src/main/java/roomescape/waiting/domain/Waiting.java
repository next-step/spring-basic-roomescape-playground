package roomescape.waiting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String time;
    private LocalDateTime createdDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    protected Waiting() {
    }

    public Waiting(String date, String time, Theme theme, Member member, Reservation reservation) {
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
        this.reservation = reservation;
        this.createdDateTime = LocalDateTime.now();
    }

    public boolean isBefore(LocalDateTime now) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        return localDateTime.isBefore(now);
    }

    public void changeToReservation() {
        this.reservation.changeMember(member);
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
