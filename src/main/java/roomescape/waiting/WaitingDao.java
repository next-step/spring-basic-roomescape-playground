package roomescape.waiting;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class WaitingDao {

    private final EntityManager em;

    public WaitingDao(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public Waiting save(Waiting waiting) {
        em.persist(waiting);
        return waiting;
    }

    @Transactional
    public void deleteById(Long id) {
        Waiting w = em.find(Waiting.class, id);
        if (w != null) {
            em.remove(w);
        }
    }

    public List<WaitingRank> findWaitingRankByMemberId(Long memberId) {
        String jpql =
                "SELECT new roomescape.waiting.WaitingRank(" +
                        "   w, " +
                        "   (SELECT COUNT(w2) " +
                        "    FROM Waiting w2 " +
                        "    WHERE w2.theme = w.theme " +
                        "      AND w2.date  = w.date  " +
                        "      AND w2.time  = w.time  " +
                        "      AND w2.id   < w.id)" +
                        ") " +
                        "FROM Waiting w " +
                        "WHERE w.member.id = :memberId";

        return em.createQuery(jpql, WaitingRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
