package roomescape.waiting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Waiting {

    private static final int BASE_RANK = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    protected Waiting() {
    }

    public Waiting(long memberId, String name, LocalDate date, Time time, Theme theme) {
        this.memberId = memberId;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation toReservation() {
        return new Reservation(memberId, name, date, time, theme);
    }

    public long calculateRank(List<Waiting> waitings) {
        return waitings.stream()
                .filter(this::isLaterThan)
                .count() + BASE_RANK;
    }

    private boolean isLaterThan(Waiting waiting) {
        return waiting.getId() < this.id;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
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

    public LocalTime getTimeValue() {
        return time.getValue();
    }

    public String getThemeName() {
        return theme.getName();
    }
}
