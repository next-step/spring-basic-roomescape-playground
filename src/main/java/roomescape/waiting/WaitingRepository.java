package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    @Query(" SELECT COUNT(w) " +
            " FROM Waiting w " +
            " WHERE w.theme = :theme " +
            "   AND w.date = :date " +
            "   AND w.time = :time " +
            "   AND w.id < :waitingId ")
    long countEarlierWaitings(Theme theme, String date, Time time, Long waitingId);
}
