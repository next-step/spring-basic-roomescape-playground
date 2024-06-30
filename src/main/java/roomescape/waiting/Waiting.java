package roomescape.waiting;

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
    private String date;
    private String time;

    public Waiting(Long id, Theme theme, String date, String time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Waiting() {
    }
}
