package roomescape.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import roomescape.exception.InvalidRequestException;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value")
    private String value;

    protected Time() {
    }

    public Time(String value) {
        this(null, value);
    }

    public Time(Long id, String value) {
        validateValue(value);
        this.id = id;
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException("시간은 비어있을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return value;
    }
}
