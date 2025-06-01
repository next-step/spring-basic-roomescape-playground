package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findAllByMemberId(Long memberId);

//    @Query(value = """
//            SELECT w.*, ranked.rank
//                FROM waiting w
//                JOIN (
//                    SELECT id, RANK() OVER (PARTITION BY theme_id, time_id, date ORDER BY id) AS rank
//                    FROM waiting
//                    WHERE theme_id = :themeId AND date = :date AND time_id = :timeId
//                ) ranked ON w.id = ranked.id
//                WHERE w.member_id = :memberId
//            """, nativeQuery = true)
//    WaitingWithRank countASDF(@Param("memberId") Long memberId,
//                              @Param("date") LocalDate date,
//                              @Param("themeId") Long themeId,
//                              @Param("timeId") Long timeId);


//    @Query("""
//            SELECT new roomescape.waiting.WaitingWithRank(
//                w,
//                (SELECT COUNT(w2)
//                 FROM Waiting w2
//                 WHERE w2.themeId = w.themeId
//                   AND w2.date = w.date
//                   AND w2.timeId = w.timeId
//                   AND w2.id < w.id))
//            FROM Waiting w
//            WHERE w.memberId = :memberId""")
//    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    void deleteByMemberIdAndId(MemberId memberId, Long id);
}
