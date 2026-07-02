package roomescape.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_reservation")
public class WaitingReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "queue_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WaitingQueue queue;

    @Column(name = "timestamp", columnDefinition = "DATETIME DEFAULT NOW()", insertable = false, nullable = false)
    private LocalDateTime timestamp;

    public WaitingReservation(Long id, WaitingQueue queue, LocalDateTime timestamp) {
        this.id = id;
        this.queue = queue;
        this.timestamp = timestamp;
    }

    public WaitingReservation(WaitingQueue queue, LocalDateTime timestamp) {
        this(null, queue, timestamp);
    }

    public WaitingReservation() {
    }

    public Long getId() {
        return id;
    }

    public WaitingQueue getQueue() {
        return queue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
