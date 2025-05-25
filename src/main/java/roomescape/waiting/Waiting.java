package roomescape.waiting;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "member_id"))
    )
    private MemberId memberId;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "theme_id"))
    )
    private ThemeId themeId;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "time_id"))
    )
    private TimeId timeId;

    private LocalDate date;

    protected Waiting() {
    }

    public Waiting(MemberId memberId, ThemeId themeId, TimeId timeId, LocalDate date) {
        this.memberId = memberId;
        this.themeId = themeId;
        this.timeId = timeId;
        this.date = date;
    }
}
