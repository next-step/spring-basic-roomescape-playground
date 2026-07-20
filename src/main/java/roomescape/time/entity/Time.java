package roomescape.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String timeValue;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean deleted = false;

    public Time() {
    }

    public Time(String value) {
        this.timeValue = value;
    }

    public void markDeleted() {
        this.deleted = true;
    }

    public Long getId() {
        return id;
    }

    public String getTimeValue() {
        return timeValue;
    }
}
