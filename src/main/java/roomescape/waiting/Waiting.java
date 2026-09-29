package roomescape.waiting;

import jakarta.persistence.*;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@Table(
        name = "waiting",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_waiting_order",
                        columnNames = {
                                "date",
                                "time_id",
                                "theme_id",
                                "waiting_order"
                        }
                )
        }
)
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Column(name = "waiting_order", nullable = false)
    private Long waitingOrder;

    protected Waiting() {
    }

    public Waiting(Long memberId, String date, Time time, Theme theme, Long waitingOrder) {
        this.memberId = memberId;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.waitingOrder = waitingOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getWaitingOrder() {
        return waitingOrder;
    }
}
