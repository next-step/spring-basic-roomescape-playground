package roomescape.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WaitingRepository {
    @PersistenceContext
    private EntityManager em;

    public Waiting save(Waiting waiting) {
        em.persist(waiting);
        return waiting;
    }

    public void deleteById(Long id) {
        Waiting waiting = em.find(Waiting.class, id);
        if (waiting != null) {
            em.remove(waiting);
        }
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql =
                "SELECT new roomescape.waiting.WaitingWithRank(" +
                        "    w, " +
                        "    (SELECT COUNT(w2) " +
                        "     FROM Waiting w2 " +
                        "     WHERE w2.theme = w.theme " +
                        "       AND w2.date = w.date " +
                        "       AND w2.time = w.time " +
                        "       AND w2.id < w.id) + 1" +
                        ") " +
                        "FROM Waiting w " +
                        "WHERE w.member.id = :memberId";

        return em.createQuery(jpql, WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
