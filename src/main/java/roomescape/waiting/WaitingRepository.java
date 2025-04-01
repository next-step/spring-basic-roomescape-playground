package roomescape.waiting;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("""
                SELECT new roomescape.waiting.WaitingRanking(
                    w,
                    (
                        SELECT COUNT(w2)
                        FROM Waiting w2
                        WHERE w2.reservation = w.reservation
                          AND w2.updatedAt < w.updatedAt
                    )
                )
                FROM Waiting w
                WHERE w.member.id = :memberId
            """)
    List<WaitingRanking> findWaitingRankingByMemberId(Long memberId);

    @Query("""
                SELECT new roomescape.waiting.WaitingRanking(
                    w,
                    (
                        SELECT COUNT(w2)
                        FROM Waiting w2
                        WHERE w2.reservation = w.reservation
                          AND w2.updatedAt < w.updatedAt
                    )
                )
                FROM Waiting w
                WHERE w.reservation.id = :reservationId
                ORDER BY w.updatedAt ASC
            """)
    Optional<WaitingRanking> findFirstWaitingRankingByReservationId(Long reservationId);

    Optional<Waiting> findByMemberId(Long memberId);
}
