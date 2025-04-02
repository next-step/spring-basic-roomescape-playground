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

    private Time(Long id, String value, boolean deleted) {
        this.id = id;
        this.value = value;
        this.deleted = deleted;
    }

    public static Time ofDeletedFalse(String value) {
        return new Time(null, value, false);
    }

    protected Time() {
    }

    public void markAsDeleted() {
        this.deleted = true;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
