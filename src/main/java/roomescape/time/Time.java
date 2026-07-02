package roomescape.time;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "time")
@SQLDelete(sql = "UPDATE time SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false, length = 20)
    private String timeValue;

    protected Time() {
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

    public String getValue() {
        return timeValue;
    }

    public String getTime() {
        return timeValue;
    }
}
