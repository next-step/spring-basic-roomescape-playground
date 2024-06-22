package roomescape.time;


import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false)
    private String value;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;


    public Time(Long id, String value, Boolean deleted) {
        this.id = id;
        this.value = value;
        this.deleted = deleted;
    }

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
        this.deleted = false;
    }

    public Time(String value) {
        this.value = value;
        this.deleted = false;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
