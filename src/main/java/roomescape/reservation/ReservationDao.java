package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ReservationDao {

    @PersistenceContext
    private EntityManager em;

    public List<Reservation> findAll() {
        return em.createQuery(
                        "SELECT r FROM Reservation r JOIN FETCH r.time JOIN FETCH r.theme",
                        Reservation.class)
                .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Reservation r = em.find(Reservation.class, id);
        if (r != null) em.remove(r);
    }

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        return em.createQuery(
                        "SELECT r " +
                                "  FROM Reservation r " +
                                "  JOIN FETCH r.time ti " +
                                "  JOIN FETCH r.theme th " +
                                " WHERE r.date = :date " +
                                "   AND th.id = :themeId",
                        Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return em.createQuery(
                        "SELECT r FROM Reservation r " +
                                " JOIN FETCH r.time t" +
                                " JOIN FETCH r.theme th" +
                                " WHERE r.date = :date AND th.id = :themeId",
                        Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT r FROM Reservation r " +
                                " JOIN FETCH r.theme t " +
                                " JOIN FETCH r.time ti " +
                                " WHERE r.member.id = :memberId", Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

}
