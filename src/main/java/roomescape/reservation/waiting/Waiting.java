package roomescape.reservation.waiting;

import jakarta.persistence.*;
import lombok.Getter;
import roomescape.theme.Theme;

@Getter
@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Theme theme;

    private Long memberId;
    private String date;
    private String time;

    public Waiting() {
    }

    public Waiting(Theme theme, Long memberId, String date, String time) {
        this.theme = theme;
        this.memberId = memberId;
        this.date = date;
        this.time = time;
    }
}
