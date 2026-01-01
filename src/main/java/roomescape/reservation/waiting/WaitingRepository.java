package roomescape.reservation.waiting;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WaitingRepository {

    private final EntityManager em;

    public WaitingRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Waiting waiting) {
        em.persist(waiting);
    }

    public Optional<Waiting> findById(Long id) {
        return Optional.ofNullable(em.find(Waiting.class, id));
    }

    public void delete(Waiting waiting) {
        em.remove(waiting);
    }

    public List<Waiting> findByMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT w FROM Waiting w JOIN FETCH w.time JOIN FETCH w.theme " +
                                "WHERE w.member.id = :memberId", Waiting.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Long findRank(Waiting waiting) {
        String jpql = """
                    SELECT COUNT(w) + 1 
                    FROM Waiting w 
                    WHERE w.date = :date 
                      AND w.theme.id = :themeId 
                      AND w.time.id = :timeId 
                      AND w.createdAt < :createdAt
                """;

        return em.createQuery(jpql, Long.class)
                .setParameter("date", waiting.getDate())
                .setParameter("themeId", waiting.getTheme().getId())
                .setParameter("timeId", waiting.getTime().getId())
                .setParameter("createdAt", waiting.getCreatedAt())
                .getSingleResult();
    }

    public boolean existsByMemberAndDateTime(Long memberId, String date, Long themeId, Long timeId) {
        String jpql = """
                    SELECT COUNT(w) > 0 
                    FROM Waiting w 
                    WHERE w.member.id = :memberId 
                      AND w.date = :date 
                      AND w.theme.id = :themeId 
                      AND w.time.id = :timeId
                """;
        return em.createQuery(jpql, Boolean.class)
                .setParameter("memberId", memberId)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .setParameter("timeId", timeId)
                .getSingleResult();
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql = "SELECT new roomescape.reservation.waiting.WaitingWithRank(" +
                "  w, " +
                "  (SELECT COUNT(w2) FROM Waiting w2 " +
                "   WHERE w2.theme = w.theme " +
                "     AND w2.date = w.date " +
                "     AND w2.time = w.time " +
                "     AND w2.id < w.id)) " +
                "FROM Waiting w " +
                "WHERE w.member.id = :memberId";

        return em.createQuery(jpql, WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
