package roomescape.reservation;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE theme SET deleted = true WHERE id = ?")
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @JoinColumn(name = "member_id", nullable = true)
    @ManyToOne
    private Member member;

    @Column(name = "date", nullable = false)
    private String date;

    @JoinColumn(name = "time_id", nullable = false)
    @ManyToOne(optional = false)
    private Time time;

    @JoinColumn(name = "theme_id", nullable = false)
    @ManyToOne(optional = false)
    private Theme theme;

    @Column(name = "pending", columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private int pending = 0;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false", insertable = false, nullable = false)
    private boolean deleted = false;

    public Reservation(Long id, String name, Member member, String date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;

        validate();
    }

    public Reservation(String name, Member member, String date, Time time, Theme theme) {
        this(null, name, member, date, time, theme);
    }

    public Reservation() {
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (name == null && member == null) {
            throw new IllegalArgumentException("both name and member cannot be null");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMemberId() {
        return member == null ? null : member.getId();
    }

    public @Nullable Member getMember() {
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
}
