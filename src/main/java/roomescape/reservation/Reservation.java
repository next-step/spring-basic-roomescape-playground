package roomescape.reservation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.member.Member;

import java.time.LocalDate;

@Entity @Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long themeId;
    private Long timeId;
    private LocalDate date;

    public Reservation(Member member, Long themeId, Long timeId, LocalDate date) {
        this.member = member;
        this.themeId = themeId;
        this.timeId = timeId;
        this.date = date;
    }
}
