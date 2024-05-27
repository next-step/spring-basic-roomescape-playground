package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    private Theme theme;

    @ManyToOne
    private Time time;

    private String date;

    public Waiting(Long memberId, Theme theme, Time time, String date) {
        this.memberId=memberId;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }
}
