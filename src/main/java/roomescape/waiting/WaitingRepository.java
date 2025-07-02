package roomescape.waiting;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;
import roomescape.time.Time;


@Repository
public class WaitingRepository {

    private final EntityManager em;

    public WaitingRepository(EntityManager em) {
        this.em = em;
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql = "SELECT new roomescape.waiting.WaitingWithRank(" +
                "    w, " +
                "    (SELECT CAST(COUNT(w2) AS Long) " +
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

    public List<Waiting> findWaitingByDateAndTimeAndTheme(String date, Time time, Theme theme) {
        String jpql = "SELECT w FROM Waiting w WHERE w.date = :date AND w.time = :time AND w.theme = :theme";

        return em.createQuery(jpql, Waiting.class)
                .setParameter("date",date)
                .setParameter("time", time)
                .setParameter("theme", theme)
                .getResultList();
    }

    public Waiting save(Waiting waiting) {
        em.persist(waiting);
        return waiting;
    }

    public void deleteById(Long id) {
        em.remove(em.find(Waiting.class, id));
    }

}
