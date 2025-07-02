package roomescape.time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class Time {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime time;

    protected Time() {
    }

    public Time(Long id, String time) {
        this.id = id;
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public Time(String time) {
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getTime() {
        return this.time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time1 = (Time) o;
        return Objects.equals(id, time1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
