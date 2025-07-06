package roomescape.reservation;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final EntityManager entityManager;

    public ReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        return Optional.ofNullable(reservation);
    }

    public List<Reservation> findAll() {
        return entityManager.createQuery("SELECT r FROM Reservation r", Reservation.class)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.member.id = :memberId", Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.date = :date AND r.themeId = :themeId", Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
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
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
