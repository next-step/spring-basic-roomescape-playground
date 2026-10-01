package roomescape.time;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false)
    private String value;

    @Column(nullable = false)
    private boolean deleted = false;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Time(String value) {
        this.value = value;
    }

    public Time() {
    }

    @JsonIgnore
    public String getTime() {
        return value;
    }

    public void delete() {
        this.deleted = true;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
