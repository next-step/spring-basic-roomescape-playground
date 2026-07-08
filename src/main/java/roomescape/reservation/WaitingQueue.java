package roomescape.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import roomescape.time.Time;

@Entity
@Table(name = "waiting_queue")
public class WaitingQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable = false)
    private String date;

    @JoinColumn(name = "time_id", nullable = false)
    @ManyToOne(optional = false)
    private Time time;

    @Column(name = "theme", nullable = false)
    @

    public WaitingQueue(Long id, String date, Time time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public WaitingQueue() {
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
}
