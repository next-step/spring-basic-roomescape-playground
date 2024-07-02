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
    private Long memberId;

    public Waiting() {
    }

    public Waiting(Long memberId, Theme theme, String date, String time) {
        this.memberId = memberId;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public boolean isMyReservation(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
