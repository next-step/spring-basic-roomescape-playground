package roomescape.reservation;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

@Repository
public class ReservationRepository {

    private final EntityManager em;

    public ReservationRepository(EntityManager em) {
        this.em = em;
    }

    public long count() {
        return em.createQuery("SELECT COUNT(t) FROM Reservation t", Long.class)
                .getSingleResult();
    }


    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM Reservation r JOIN FETCH r.time JOIN FETCH r.theme WHERE r.deleted = false";

        return em.createQuery(jpql, Reservation.class)
                .getResultList();
    }

    public Reservation save(ReservationRequest reservationRequest, String reservationName) {
        Reservation reservation = new Reservation(
                reservationName,
                reservationRequest.getDate(),
                new Time(reservationRequest.getTime().toString()),
                new Theme(reservationName, reservationRequest.getTheme().toString())
        );
        em.persist(reservation);
        return reservation;
    }

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public void deleteById(Long id) {
        Reservation reservation = em.find(Reservation.class, id);
        if (reservation != null) {
            reservation.setDeleted(true);
        }
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return em.createQuery(
                        "SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId AND r.deleted = false",
                        Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {

        String jpql = """
                    SELECT r FROM Reservation r 
                    JOIN FETCH r.time 
                    JOIN FETCH r.theme 
                    WHERE r.member.id = :memberId AND r.deleted = false
                """;

        return em.createQuery(jpql, Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}