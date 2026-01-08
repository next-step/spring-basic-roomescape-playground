package roomescape.waiting;

import jakarta.persistence.*;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiting")
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Waiting() {
    }

    public Waiting(Member member, String date, Time time, Theme theme) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}


