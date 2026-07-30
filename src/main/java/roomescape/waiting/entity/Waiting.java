package roomescape.waiting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.member.entity.Member;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;

import java.time.LocalDate;

@Entity(name = "waiting")
@NoArgsConstructor
@Getter
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    public Waiting(LocalDate date, Member member, Time time, Theme theme) {
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public static Waiting of(LocalDate date, Member member, Time time, Theme theme) {
        return new Waiting(date, member, time, theme);
    }
}
