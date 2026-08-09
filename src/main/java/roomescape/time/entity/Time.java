package roomescape.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.global.entity.BaseSoftDeleteEntity;

@Entity(name = "time")
@NoArgsConstructor
@Getter
public class Time extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String timeValue;

    private Time(String value) {
        this.timeValue = value;
    }

    public static Time from(String timeValue) {
        return new Time(timeValue);
    }
}
