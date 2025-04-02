package roomescape.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import roomescape.auth.dto.LoginMember;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.waiting.domain.Status;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
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

    protected Reservation() {
    }

    public Reservation(LoginMember loginMember, String name, LocalDate date, Time time, Theme theme) {
        this(date, time, theme);
        if (isInvalidName(name)) {
            this.memberId = loginMember.id();
            this.name = loginMember.name();
            return;
        }
        this.name = name;
    }

    public Reservation(LocalDate date, Time time, Theme theme) {
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private boolean isInvalidName(final String name) {
        return name == null || name.isBlank();
    }

    public Reservation(long memberId, String name, LocalDate date, Time time, Theme theme) {
        this.memberId = memberId;
        this.name = name;
        this.date = date;
        this.time = time;
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

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public String getThemeName() {
        return theme.getName();
    }

    public LocalTime getTimeValue() {
        return time.getValue();
    }

    public String getRankStatus() {
        return Status.CONFIRMED.getDescription();
    }
}
