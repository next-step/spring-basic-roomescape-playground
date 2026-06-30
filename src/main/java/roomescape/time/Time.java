package roomescape.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"time\"")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "\"value\"")
    private String value;

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
