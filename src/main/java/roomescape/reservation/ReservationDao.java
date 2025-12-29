package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ReservationDao {

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
    public Reservation save(ReservationRequest reservationRequest) {
        // Time과 Theme 조회
        Time time = entityManager.find(Time.class, reservationRequest.getTime());
        Theme theme = entityManager.find(Theme.class, reservationRequest.getTheme());

        // Reservation 생성 및 저장
        Reservation reservation = new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme
        );

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

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.time t " +
                "JOIN FETCH r.theme th " +
                "WHERE r.date = :date AND th.id = :themeId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("date", date);
        query.setParameter("themeId", themeId);
        return query.getResultList();
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
}
