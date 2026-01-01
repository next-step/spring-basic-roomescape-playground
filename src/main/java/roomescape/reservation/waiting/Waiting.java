package roomescape.reservation.waiting;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDateTime;

@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    private String date;

    private LocalDateTime createdAt = LocalDateTime.now();


    public Waiting() {
    }

    public Waiting(Member member, Theme theme, Time time, String date) {
        this.member = member;
        this.theme = theme;
        this.time = time;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Theme getTheme() {
        return theme;
    }

    public Time getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
