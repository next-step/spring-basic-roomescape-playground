package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import roomescape.dto.ReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.Time;

import java.util.List;

@Repository
public class ReservationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM Reservation r";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        return query.getResultList();
    }

    public Reservation save(ReservationRequest reservationRequest) {
        Time time = entityManager.find(Time.class, reservationRequest.time());
        Theme theme = entityManager.find(Theme.class, reservationRequest.theme());

        Reservation reservation = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                time,
                theme
        );

        entityManager.persist(reservation);
        return reservation;
    }

    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("date", date);
        query.setParameter("themeId", themeId);
        return query.getResultList();
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("date", date);
        query.setParameter("themeId", themeId);
        return query.getResultList();
    }
}
