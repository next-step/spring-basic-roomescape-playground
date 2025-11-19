package roomescape.time;

import jakarta.persistence.*;

@Entity
public class ParticipationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String time;

    public ParticipationTime(Long id, String time) {
        this.id = id;
        this.time = time;
    }

    public ParticipationTime(String time) {
        this.time = time;
    }

    public ParticipationTime() {

    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }


}
