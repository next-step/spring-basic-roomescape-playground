package roomescape.time;

import jakarta.persistence.*;

@Entity
@Table(name = "time")
public class Time {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value")
    private String time;

    @Column(name = "deleted")
    private Boolean deleted;

    public Time(Long id, String time) {
        this.id = id;
        this.time = time;
    }

    public Time(String time) {
        this.time = time;
    }

    public Time() {
    }

    public Long getId() {
        return id;
    }

    public String getTime(){
        return time;
    }
}
