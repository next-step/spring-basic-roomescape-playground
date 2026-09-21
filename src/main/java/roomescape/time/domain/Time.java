package roomescape.time.domain;

import jakarta.persistence.*;

@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", unique = true)
    private String value;

    private boolean deleted;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = true;
    }

    public void restore() {
        deleted = false;
    }
}
