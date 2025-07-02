package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.Theme;
import roomescape.time.Time;

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

    public boolean existsByThemeAndDateAndTimeAndMember_Id(Theme theme, LocalDate date, Time time, Long memberId) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Reservation r " +
                                "WHERE r.theme = :theme AND r.date = :date AND r.time = :time AND r.member.id = :memberId", Long.class)
                .setParameter("theme", theme)
                .setParameter("date", date)
                .setParameter("time", time)
                .setParameter("memberId", memberId)
                .getSingleResult();
        return count > 0;
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
