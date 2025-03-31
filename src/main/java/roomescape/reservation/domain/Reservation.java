package roomescape.reservation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.waiting.Waiting;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @OneToMany(mappedBy = "reservation")
    private List<Waiting> waitings;

    protected Reservation() {
    }

    public Reservation(Long id, String date, Member member, Time time, Theme theme) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String date, Member member, Time time, Theme theme) {
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public boolean isSame(Long id) {
        return this.member.getId().equals(id);
    }

    public boolean isSavedSameMember(Member member) {
        return this.member.getEmail().equals(member.getEmail());
    }

    public boolean remainWaitings() {
        return !waitings.isEmpty();
    }

    public boolean isBefore(LocalDateTime now) {
        LocalDateTime reservedDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time.getValue()));
        return reservedDateTime.isBefore(now);
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Member getMember() {
        return member;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
