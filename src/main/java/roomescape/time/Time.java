package roomescape.time;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "time")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_value")
    private String value;
    @JsonIgnore
    private boolean deleted;

    public Time() {
    }

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
        this.deleted = false;
    }

    public Long id() {
        return id;
    }

    public String value() {
        return value;
    }

    public boolean deleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }
}
