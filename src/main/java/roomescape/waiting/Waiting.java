package roomescape.waiting;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.member.Member;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long themeId;
    private Long timeId;
    private LocalDate date;

    public Waiting(Member member, Long themeId, Long timeId, LocalDate date) {
        this.member = member;
        this.themeId = themeId;
        this.timeId = timeId;
        this.date = date;
    }
}
