package roomescape.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Time time;
    @ManyToOne
    private Theme theme;

    public Reservation(Member member,String name, String date, Time time, Theme theme) {
        this.member = member;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }
}
