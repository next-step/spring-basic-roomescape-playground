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
                    ) + 1L
                )
                FROM Waiting w
                JOIN FETCH w.reservation r
                JOIN FETCH r.theme t
                JOIN FETCH r.reservationTime rt
                WHERE w.member.id = :memberId
            """)
    List<WaitingRanking> findWaitingRankingByMemberId(long memberId);

    @Query("""
                SELECT new roomescape.waiting.WaitingRanking(
                    w,
                    (
                        SELECT COUNT(w2)
                        FROM Waiting w2
                        WHERE w2.reservation = w.reservation
                          AND w2.updatedAt < w.updatedAt
                    ) + 1L
                )
                FROM Waiting w
                WHERE w.reservation.id = :reservationId
            """)
    Optional<WaitingRanking> findAllByReservationId(long reservationId);

    void deleteByReservation_IdAndMember_Id(long reservationId, long memberId);
}
