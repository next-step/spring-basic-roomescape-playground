package roomescape.reservation;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

    private final EntityManager em;

    public ReservationDao(EntityManager em) {
        this.em = em;
    }

    public List<Reservation> findAll() {
        return em.createQuery(
                        "SELECT r FROM Reservation r JOIN FETCH r.time JOIN FETCH r.theme",
                        Reservation.class)
                .getResultList();
    }

    public int deleteById(Long id) {
        return em.createQuery(
                        "DELETE FROM Reservation r WHERE r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
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
