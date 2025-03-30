package roomescape.waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.domain.WaitingWithRank;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByMemberIdAndDateAndTimeAndTheme(Long memberId, LocalDate date, Time time, Theme theme);

    @Query("SELECT CAST((SELECT COUNT(w2) + 1 " +
            "          FROM Waiting w2 " +
            "          WHERE w2.theme = w.theme " +
            "            AND w2.date = w.date " +
            "            AND w2.time = w.time " +
            "            AND w2.id < w.id) AS long) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    Long findWaitingNumberByMemberId(Long memberId);

    @Query("SELECT new roomescape.waiting.domain.WaitingWithRank(" +
            "    w, " +
            "    CAST((SELECT COUNT(w2) + 1 " +
            "          FROM Waiting w2 " +
            "          WHERE w2.theme = w.theme " +
            "            AND w2.date = w.date " +
            "            AND w2.time = w.time " +
            "            AND w2.id < w.id) AS long)) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    @Query("SELECT w " +
            "FROM Waiting w " +
            "WHERE w.date = :date " +
            "  AND w.time = :time " +
            "  AND w.theme = :theme " +
            "ORDER BY w.id ASC")
    Optional<Waiting> findFirstWaitingByDateAndTimeAndTheme(LocalDate date, Time time, Theme theme);
}
