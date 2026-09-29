package roomescape.domain.time.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Time {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalTime timeValue;

    protected Time() {
    }

    public Time(LocalTime timeValue) {
        if (timeValue == null) {
            throw new IllegalArgumentException("Time을 만들기 위해 value는 필수 필드입니다.");
        }
        this.timeValue = timeValue;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getTimeValue() {
        return timeValue;
    }
}
