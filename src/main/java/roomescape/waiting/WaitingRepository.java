package roomescape.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WaitingRepository {
    @PersistenceContext
    private EntityManager em;

    public Waiting save(Waiting waiting) {
        if (waiting.getId() == null) {
            em.persist(waiting);
            return waiting;
        }
        return em.merge(waiting);
    }

    public Optional<Waiting> findById(Long id) {
        return Optional.ofNullable(em.find(Waiting.class, id));
    }

    public List<Waiting> findAll() {
        return em.createQuery("SELECT t FROM Waiting t", Waiting.class).getResultList();
    }

    public void deleteById(Long id) {
        Waiting find = em.find(Waiting.class, id);
        if (find != null) {
            em.remove(find);
        }
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql = "SELECT new roomescape.waiting.WaitingWithRank(" +
                "    w, " +
                "    (SELECT COUNT(w2) " +
                "     FROM Waiting w2 " +
                "     WHERE w2.theme = w.theme " +
                "       AND w2.date = w.date " +
                "       AND w2.time = w.time " +
                "       AND w2.id < w.id)) " +
                "FROM Waiting w " +
                "WHERE w.member.id = :memberId";

        return em.createQuery(jpql, WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
