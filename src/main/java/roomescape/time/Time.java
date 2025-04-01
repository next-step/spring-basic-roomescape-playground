package roomescape.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false, length = 20)
    private String value;

    @Column(name = "deleted", nullable = false, columnDefinition = "DEFAULT FALSE")
    private boolean deleted;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Time(String value) {
        this.value = value;
    }

    protected Time() {
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
