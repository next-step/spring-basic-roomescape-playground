package roomescape.time.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "time")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false)
    private String time;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    public Time(String time) {
        this.time = time;
    }
}
