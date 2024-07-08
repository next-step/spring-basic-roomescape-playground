package roomescape.time;

import jakarta.persistence.*;

@Entity
@Table(name = "event_time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String event_value;

    public Time(Long id, String event_value) {
        this.id = id;
        this.event_value = event_value;
    }

    public Time(String event_value) {
        this.event_value = event_value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getEvent_value() {
        return event_value;
    }

}
