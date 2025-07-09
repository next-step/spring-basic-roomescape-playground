package roomescape.waiting;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.waiting.dto.WaitingWithRank;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class WaitingRepository {

    private final EntityManager entityManager;

    public WaitingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Waiting save(Waiting waiting) {
        entityManager.persist(waiting);
        return waiting;
    }

    public Optional<Waiting> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Waiting.class, id));
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql = "SELECT new roomescape.waiting.dto.WaitingWithRank(" +
                "    w, " +
                "    (SELECT COUNT(w2) " +
                "     FROM Waiting w2 " +
                "     WHERE w2.themeId = w.themeId " +
                "       AND w2.date = w.date " +
                "       AND w2.timeId = w.timeId " +
                "       AND w2.id < w.id)) " +
                "FROM Waiting w " +
                "WHERE w.member.id = :memberId";
        return entityManager.createQuery(jpql, WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public boolean existsByThemeIdAndDateAndTimeIdAndMember_Id(Long themeId, LocalDate date, Long timeId, Long memberId) {
        String jpql = "SELECT COUNT(w) FROM Waiting w " +
                "WHERE w.themeId = :themeId AND w.date = :date AND w.timeId = :timeId AND w.member.id = :memberId";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("themeId", themeId)
                .setParameter("date", date)
                .setParameter("timeId", timeId)
                .setParameter("memberId", memberId)
                .getSingleResult();
        return count > 0;
    }

    @Transactional
    public void delete(Waiting waiting) {
        if (!entityManager.contains(waiting)) {
            waiting = entityManager.merge(waiting);
        }
        entityManager.remove(waiting);
    }
}
