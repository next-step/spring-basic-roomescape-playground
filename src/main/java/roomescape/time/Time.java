package roomescape.time;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_value", nullable = false)
    private LocalTime value;

    protected Time() {
    }

    public Time(Long id, LocalTime value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getValue() {
        return value;
    }
}
