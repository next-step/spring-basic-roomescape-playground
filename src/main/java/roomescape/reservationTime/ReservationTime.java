package roomescape.reservationTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;

@Entity
public class ReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime timeValue;

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
