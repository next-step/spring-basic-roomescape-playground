package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@Table(name = "waiting")
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private String date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    protected Waiting() {
    }

    public Waiting(Long memberId, String date, Time time, Theme theme) {
        this.memberId = memberId;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Long id() {
        return id;
    }

    public String date() {
        return date;
    }

    public Time time() {
        return time;
    }

    public Theme theme() {
        return theme;
    }
}
