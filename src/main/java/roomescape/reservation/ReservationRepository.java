package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

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
        // N+1 문제를 피하기 위해 fetch join 사용
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r " +
                                "JOIN FETCH r.time " +
                                "JOIN FETCH r.theme " +
                                "JOIN FETCH r.member", Reservation.class)
                .getResultList();
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r " +
                                "WHERE r.date = :date AND r.theme.id = :themeId", Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r " +
                                "JOIN FETCH r.time " +
                                "JOIN FETCH r.theme " +
                                "WHERE r.member.id = :memberId", Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
