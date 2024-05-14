package roomescape.time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "time")
public class Time {
    @Id @GeneratedValue
    private Long id;
    private String value;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

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
