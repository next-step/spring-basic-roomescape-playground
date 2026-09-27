package roomescape.waiting;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"member_id", "date", "time_id", "theme_id"}))
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    public Waiting(Member member, LocalDate date, Time time, Theme theme) {
        validateNotPast(date);
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Waiting() {

    }

    private void validateNotPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지난 날짜는 대기할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
