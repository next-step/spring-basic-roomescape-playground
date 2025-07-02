package roomescape.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.waiting.dto.WaitingWithRank;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class WaitingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Waiting save(Waiting waiting) {
        entityManager.persist(waiting);
        return waiting;
    }

    public Optional<Waiting> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Waiting.class, id));
    }

    @Transactional
    public void delete(Waiting waiting) {
        // 삭제하려는 엔티티가 영속성 컨텍스트에 없는 경우, 먼저 병합(merge)하여 관리 상태로 만듭니다.
        if (!entityManager.contains(waiting)) {
            waiting = entityManager.merge(waiting);
        }
        entityManager.remove(waiting);
    }

    /**
     * JPQL을 사용하여 특정 사용자의 예약 대기 목록을 순위와 함께 조회합니다.
     * @param memberId 사용자 ID
     * @return 순위 정보가 포함된 예약 대기 목록
     */
    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        String jpql = "SELECT new roomescape.waiting.dto.WaitingWithRank(" +
                "    w, " +
                "    (SELECT COUNT(w2) " +
                "     FROM Waiting w2 " +
                "     WHERE w2.theme = w.theme " +
                "       AND w2.date = w.date " +
                "       AND w2.time = w.time " +
                "       AND w2.id < w.id)) " +
                "FROM Waiting w " +
                "WHERE w.member.id = :memberId";
        return entityManager.createQuery(jpql, WaitingWithRank.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
     * 특정 조건으로 예약 대기가 존재하는지 확인합니다. (중복 방지용)
     */
    public boolean existsByThemeAndDateAndTimeAndMember_Id(Theme theme, LocalDate date, Time time, Long memberId) {
        String jpql = "SELECT COUNT(w) FROM Waiting w " +
                "WHERE w.theme = :theme AND w.date = :date AND w.time = :time AND w.member.id = :memberId";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("theme", theme)
                .setParameter("date", date)
                .setParameter("time", time)
                .setParameter("memberId", memberId)
                .getSingleResult();
        return count > 0;
    }
}
