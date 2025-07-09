package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.waiting.dto.WaitingWithRank;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByThemeIdAndDateAndTimeIdAndMember_Id(Long themeId, LocalDate date, Long timeId, Long memberId);

    @Query("SELECT new roomescape.waiting.dto.WaitingWithRank(" +
            "    w, " +
            "    (SELECT CAST(COUNT(w2) AS long) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.themeId = w.themeId " +
            "       AND w2.date = w.date " +
            "       AND w2.timeId = w.timeId " +
            "       AND w2.id < w.id)" +
            ") " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);
}
