package roomescape.time;

import java.time.LocalTime;

public class Time {
    private Long id;
    private LocalTime value;

    public Time(Long id, LocalTime value) {
        this.id = id;
        this.value = value;
    }

    public Time(LocalTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Time을 만들기 위해 value는 필수 필드입니다.");
        }
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getValue() {
        return value;
    }
}
