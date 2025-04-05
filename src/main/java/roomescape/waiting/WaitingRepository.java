package roomescape.waiting;

import java.util.List;
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
                          AND w2.id < w.id
                    ) + 1L
                )
                FROM Waiting w
                JOIN FETCH w.member
                JOIN FETCH w.reservation r
                JOIN FETCH r.theme t
                JOIN FETCH r.reservationTime rt
                WHERE w.member.id = :memberId
            """)
    List<WaitingRanking> findWaitingRankingByMemberId(long memberId);

    @Query("""
                SELECT w
                FROM Waiting w
                WHERE w.reservation.id = :reservationId
            """)
    List<Waiting> findAllByReservationId(long reservationId);

    void deleteByReservation_IdAndMember_Id(long reservationId, long memberId);
}
