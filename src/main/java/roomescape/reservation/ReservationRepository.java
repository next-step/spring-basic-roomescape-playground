package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.time t " +
                "JOIN FETCH r.theme th";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        return query.getResultList();
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    @Transactional
    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.time t " +
                "JOIN FETCH r.theme th " +
                "WHERE r.date = :date AND th.id = :themeId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("date", date);
        query.setParameter("themeId", themeId);
        return query.getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.time t " +
                "JOIN FETCH r.theme th " +
                "WHERE r.member.id = :memberId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("memberId", memberId);
        return query.getResultList();
    }
}
