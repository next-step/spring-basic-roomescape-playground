package roomescape.reservation;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_waiting", nullable = false)
    private boolean isWaiting = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Reservation() {
    }

    public Reservation(String name, String date, boolean isWaiting, Time time, Theme theme, Member member) {
        this.name = name;
        this.date = date;
        this.isWaiting = isWaiting;
        this.createdAt = LocalDateTime.now();
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }

    public void confirm() {
        this.isWaiting = false;
    }

    public boolean isOwnedBy(Long memberId) {
        return member != null && member.getId().equals(memberId);
    }
}
