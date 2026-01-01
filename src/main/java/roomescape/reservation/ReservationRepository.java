package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    public Reservation save(Reservation reservation) {
        em.persist(reservation);
        return reservation;
    }

    public List<Reservation> findAll() {
        return em.createQuery("SELECT r FROM Reservation r", Reservation.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        Reservation reservation = em.find(Reservation.class, id);
        if (reservation != null) {
            em.remove(reservation);
        }
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId";

        return em.createQuery(jpql, Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.member.id = :memberId";
        return em.createQuery(jpql, Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
