package roomescape.time;

import jakarta.persistence.*;

@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "time_value")
    private String value;

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

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
