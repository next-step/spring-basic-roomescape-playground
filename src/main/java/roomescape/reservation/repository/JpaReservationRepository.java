package roomescape.reservation.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JpaReservationRepository implements ReservationRepository {

    private final EntityManager entityManager;

    public JpaReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM reservation AS r";

        return entityManager.createQuery(jpql, Reservation.class)
                .getResultList();
    }

    @Override
    public List<Reservation> findAllByMemberId(Long memberId) {
        String jpql = "SELECT r FROM reservation AS r WHERE r.member.id = :memberId";

        return entityManager.createQuery(jpql, Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String jpql = "SELECT r FROM reservation AS r WHERE r.date = :date AND r.theme.id = :themeId";

        return entityManager.createQuery(jpql, Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        entityManager.persist(reservation);

        return reservation;
    }

    @Override
    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }
}
