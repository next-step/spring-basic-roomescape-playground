package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.dto.WaitingRequest;
import roomescape.dto.WaitingWithRank;
import roomescape.model.Member;
import roomescape.model.Theme;
import roomescape.model.Time;
import roomescape.model.Waiting;

@Repository
public class WaitingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Waiting> findById(Long id) {
        Waiting waiting = entityManager.find(Waiting.class, id);
        return Optional.ofNullable(waiting);
    }

    public boolean existsByMemberAndDateAndTimeAndTheme(Long memberId, String date, Long timeId, Long themeId) {
        String jpql = "SELECT COUNT(w) FROM Waiting w WHERE w.member.id = :memberId AND w.date = :date AND w.time.id = :timeId AND w.theme.id = :themeId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("memberId", memberId);
        query.setParameter("date", date);
        query.setParameter("timeId", timeId);
        query.setParameter("themeId", themeId);
        return query.getSingleResult() > 0;
    }

    public Waiting save(WaitingRequest request, Member member) {
        Time time = entityManager.find(Time.class, request.time());
        Theme theme = entityManager.find(Theme.class, request.theme());

        Waiting waiting = new Waiting(member, request.date(), time, theme);

        entityManager.persist(waiting);
        return waiting;
    }

    public void deleteById(Long id) {
        Waiting waiting = entityManager.find(Waiting.class, id);
        if (waiting != null) {
            entityManager.remove(waiting);
        }
    }

    public Long countByDateAndTimeAndTheme(String date, Long timeId, Long themeId) {
        String jpql = "SELECT COUNT(w) FROM Waiting w WHERE w.date = :date AND w.time.id = :timeId AND w.theme.id = :themeId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("date", date);
        query.setParameter("timeId", timeId);
        query.setParameter("themeId", themeId);
        return query.getSingleResult();
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql =
                "SELECT new roomescape.dto.WaitingWithRank(" +
                        "   w, " +
                        "   (SELECT COUNT(w2) + 1 " +
                        "    FROM Waiting w2 " +
                        "    WHERE w2.theme = w.theme " +
                        "      AND w2.date = w.date " +
                        "      AND w2.time = w.time " +
                        "      AND w2.id < w.id)" +
                        ") " +
                        "FROM Waiting w " +
                        "WHERE w.member.id = :memberId";
        TypedQuery<WaitingWithRank> query = entityManager.createQuery(jpql, WaitingWithRank.class);
        query.setParameter("memberId", memberId);

        return query.getResultList();
    }
}
