package roomescape.domain.waiting.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.domain.waiting.dto.WaitingWithRank;
import roomescape.domain.waiting.entity.Waiting;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    /**
     * JPQL의 SELECT 문에 new를 사용할 경우, 해당 클래스로 바로 반환이 가능(패키지명까지 작성해야 함)
     * 요청한 회원의 ID와 일치하는 예약 대기와 그 순위 값이 담겨있는 WaitingWithRank의 리스트를 반환
     */
    @Query(
            "SELECT new roomescape.domain.waiting.dto.WaitingWithRank(" +
            "   w, " +
            "   (SELECT COUNT(w2)" +
            "   FROM Waiting w2 " +
            "   WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)" +
            ") " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId"
    )
    List<WaitingWithRank> findWaitingWithRankByMemberId(@Param("memberId") Long memberId);

    @Query(
            "SELECT COUNT(w) FROM Waiting w " +
            "WHERE w.theme.id = :themeId " +
            "   AND w.time.id = :timeId " +
            "   AND w.date = :date"
    )
    Long countByThemeIdAndTimeIdAndDate(@Param("themeId") Long themeId,
                                        @Param("timeId") Long timeId,
                                        @Param("date") String date);
}
