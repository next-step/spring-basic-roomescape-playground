package roomescape.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationsByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberId);

    @Query("""
        SELECT new roomescape.reservation.WaitingWithRank(
            r,
            (SELECT COUNT(r2)
             FROM Reservation r2
             WHERE r2.theme = r.theme
                AND r2.date = r.date
                AND r2.time = r.time
                AND r2.id < r.id)
            )
        FROM Reservation r
        WHERE r.member.id = :memberId
        """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    Long countByDateAndThemeIdAndTimeIdAndIdLessThan(
        String date,
        Long themeId,
        Long timeId,
        Long id
    );

    boolean existsByDateAndTimeAndThemeAndMember(
        String date, Time time, Theme theme, Member member
    );
}
