package roomescape.time;

import jakarta.persistence.*;

@Table(name = "time")
@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String time_value;

    public Time(Long id, String time_value) {
        this.id = id;
        this.time_value = time_value;
    }

    public Time(String time_value) {
        this.time_value = time_value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getTime_value() {
        return time_value;
    }
}
