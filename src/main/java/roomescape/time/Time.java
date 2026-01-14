package roomescape.time;

import jakarta.persistence.*;

@Entity
@Table(name = "time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false, length = 20)
    private String value;

    @Column(nullable = false)
    private Boolean deleted = false;

    protected Time() {
    }

    public Time(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void markDeleted() {
        this.deleted = true;
    }
}
