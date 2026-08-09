package roomescape.time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String timeValue;

    public Time(Long id, String value) {
        this.id = id;
        this.timeValue = value;
    }

    public Time(String value) {
        this.timeValue = value;
    }

    public Time() {

    }
}
