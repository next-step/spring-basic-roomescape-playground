package roomescape.time;

import jakarta.persistence.*;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false)
    private String value;
    private Boolean deleted;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
        this.deleted = false;
    }

    public Time(String value) {
        this.value = value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return value;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
