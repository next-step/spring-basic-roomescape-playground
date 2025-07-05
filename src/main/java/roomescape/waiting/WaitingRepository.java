package roomescape.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import roomescape.exception.RoomEscapeException;

import java.util.List;

import static roomescape.exception.ErrorCode.*;

@Repository
public class WaitingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Waiting save(Waiting waiting) {
        entityManager.persist(waiting);
        return waiting;
    }

    public List<Waiting> findByMemberId(Long memberId) {
        return entityManager.createQuery("""
                        SELECT w FROM Waiting w
                        JOIN FETCH w.theme t
                        JOIN FETCH w.time ti
                        WHERE w.member.id =:memberId
                        """, Waiting.class
                ).setParameter("memberId", memberId)
                .getResultList();
    }

    public List<WaitingWithRank> findWaitingWithRankByMemberId(Long memberId) {

        String jpql = """
                SELECT new roomescape.waiting.WaitingWithRank(
                    w,
                    (SELECT COUNT(w2) + 1
                     FROM Waiting w2
                     WHERE w2.theme = w.theme
                       AND w2.date = w.date
                       AND w2.time = w.time
                       AND w2.id < w.id)
                )
                FROM Waiting w
                WHERE w.member.id = :memberId
                """;

        return entityManager.createQuery(jpql, WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();

    }

    public void deleteById(Long id) {
        Waiting waiting = entityManager.find(Waiting.class, id);
        if (waiting == null) {
            throw new RoomEscapeException(WAITING_NOT_FOUND);
        }
        entityManager.remove(waiting);
    }

    public boolean existsByMemberIdAndThemeIdAndDateAndTimeId(Long memberId, Long themeId, String date, Long timeId) {
        return entityManager.createQuery("""
                            SELECT COUNT(w) > 0 FROM Waiting w
                            WHERE w.member.id = :memberId
                              AND w.theme.id = :themeId
                              AND w.date = :date
                              AND w.time.id = :timeId
                        """, Boolean.class)
                .setParameter("memberId", memberId)
                .setParameter("themeId", themeId)
                .setParameter("date", date)
                .setParameter("timeId", timeId)
                .getSingleResult();
    }

    public Long getWaitingRank(Waiting waiting) {
        return entityManager.createQuery("""
                            SELECT COUNT(w) + 1 FROM Waiting w
                            WHERE w.theme = :theme
                              AND w.date = :date
                              AND w.time = :time
                              AND w.id < :id
                        """, Long.class)
                .setParameter("theme", waiting.getTheme())
                .setParameter("date", waiting.getDate())
                .setParameter("time", waiting.getTime())
                .setParameter("id", waiting.getId())
                .getSingleResult();
    }
}
