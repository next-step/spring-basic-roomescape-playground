package roomescape.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class WaitingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Waiting save(Waiting waiting) {
        entityManager.persist(waiting);
        return waiting;
    }

    public Optional<Waiting> findById(Long id) {
        String jpql = "SELECT w FROM Waiting w " +
                "JOIN FETCH w.member " +
                "WHERE w.id = :id";

        TypedQuery<Waiting> query = entityManager.createQuery(jpql, Waiting.class);
        query.setParameter("id", id);

        List<Waiting> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Transactional
    public void deleteById(Long id) {
        Waiting waiting = entityManager.find(Waiting.class, id);
        if (waiting != null) {
            entityManager.remove(waiting);
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
                "JOIN FETCH w.time " +
                "JOIN FETCH w.theme " +
                "WHERE w.member.id = :memberId";

        TypedQuery<WaitingWithRank> query = entityManager.createQuery(jpql, WaitingWithRank.class);
        query.setParameter("memberId", memberId);
        return query.getResultList();
    }

    public List<Waiting> findByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId) {
        String jpql = "SELECT w FROM Waiting w " +
                "WHERE w.date = :date " +
                "AND w.time.id = :timeId " +
                "AND w.theme.id = :themeId";

        TypedQuery<Waiting> query = entityManager.createQuery(jpql, Waiting.class);
        query.setParameter("date", date);
        query.setParameter("timeId", timeId);
        query.setParameter("themeId", themeId);
        return query.getResultList();
    }
}
