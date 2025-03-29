package roomescape.waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.domain.WaitingWithRank;

import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

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
}
