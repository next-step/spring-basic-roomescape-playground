package roomescape.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String timeValue;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    public Time() {
    }

    public Time(String timeValue) {
        this.timeValue = timeValue;
    }

    public Time(Long id, String timeValue) {
        this.id = id;
        this.timeValue = timeValue;
    }

    public Long getId() {
        return id;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
