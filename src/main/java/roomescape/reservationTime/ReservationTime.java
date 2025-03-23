package roomescape.reservationTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE reservation_time SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime timeValue;

    private boolean deleted = false;

    public ReservationTime(Long id, LocalTime timeValue) {
        this.id = id;
        this.timeValue = timeValue;
    }

    public ReservationTime(String timeValue) {
        this.timeValue = LocalTime.parse(timeValue);
    }

    public ReservationTime() {

    }

    public Long getId() {
        return id;
    }

    public LocalTime getTimeValue() {
        return timeValue;
    }
}
