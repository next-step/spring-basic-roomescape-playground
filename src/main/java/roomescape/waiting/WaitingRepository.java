package roomescape.waiting;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.reservation.Reservation;
import roomescape.theme.Theme;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    List<Waiting> findByThemeIdAndDateAndTime(Long themeId, String date, String time);

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    CAST((SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id) AS LONG)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    Optional<Waiting> findTopByReservationOrderByCreatedDateTime(Reservation reservation);

    boolean existsByMemberEmailAndDateAndTimeAndThemeId(String email, String date, String time, Long theme);
}
