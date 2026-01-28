package roomescape.time;

import jakarta.persistence.*;
import roomescape.reservation.Reservation;

import java.util.List;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_value")
    private String value;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    @OneToMany(mappedBy = "time")
    private List<Reservation> reservations;

    public Time(String value) {
        this.value = value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

}
