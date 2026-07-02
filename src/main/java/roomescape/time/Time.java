package roomescape.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "time")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE time SET deleted = true WHERE id = ?")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "time_value", length = 20, nullable = false)
    private String value;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false", insertable = false, nullable = false)
    private boolean deleted = false;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Time(String value) {
        this.value = value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
