package roomescape.time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime time;

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
}
