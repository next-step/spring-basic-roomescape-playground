package roomescape.waiting;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;
import roomescape.time.Time;


@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT CAST(COUNT(w2) AS Long) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "JOIN FETCH w.member m " +
            "JOIN FETCH w.theme th " +
            "JOIN FETCH w.time t " +
            "WHERE m.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT w FROM Waiting w " +
            "JOIN FETCH w.member m " +
            "JOIN FETCH w.theme t " +
            "JOIN FETCH w.time ti " +
            "WHERE w.date = :date AND t = :theme AND ti = :time")
    List<Waiting> findWaitingByDateAndTimeAndTheme(@Param("date") String date,
                                                   @Param("time") Time time,
                                                   @Param("theme") Theme theme);

}
