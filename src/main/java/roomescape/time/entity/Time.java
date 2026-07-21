package roomescape.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import roomescape.global.entity.BaseSoftDeleteEntity;

@Entity(name = "time")
public class Time extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String timeValue;

    public Time() {
    }

    public Time(String value) {
        this.timeValue = value;
    }

    public Long getId() {
        return id;
    }

    public String getTimeValue() {
        return timeValue;
    }
}
