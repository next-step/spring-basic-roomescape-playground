package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import java.util.List;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> findAll() {

        return entityManager.createQuery(
                        "SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time", Reservation.class)
                .getResultList();
    }

    public Reservation save(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation == null) {
            throw new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        entityManager.remove(reservation);
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {

        return entityManager.createQuery(""" 
                        SELECT r FROM Reservation r
                        JOIN FETCH r.theme t
                        JOIN FETCH r.time ti
                        WHERE r.date = :date
                        AND r.theme.id = :themeId
                        """, Reservation.class
                ).setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return entityManager.createQuery("""
                        SELECT r FROM Reservation r
                        JOIN FETCH r.theme t
                        JOIN FETCH r.time ti
                        WHERE r.member.id =:memberId
                        """, Reservation.class
                ).setParameter("memberId", memberId)
                .getResultList();
    }

    public boolean existsThemeIdAndDateAndTimeId(Long themeId, String date, Long timeId) {
        return entityManager.createQuery("""
                            SELECT COUNT(r) > 0 FROM Reservation r
                              Where r.theme.id = :themeId
                              AND r.date = :date
                              AND r.time.id = :timeId
                        """, Boolean.class)
                .setParameter("themeId", themeId)
                .setParameter("date", date)
                .setParameter("timeId", timeId)
                .getSingleResult();
    }

    public boolean existsMemberIdAndThemeIdAndDateAndTimeId(Long memberId, Long themeId, String date, Long timeId) {
        return entityManager.createQuery("""
                            SELECT COUNT(r) > 0 FROM Reservation r
                              Where r.member.id = :memberId
                              And r.theme.id = :themeId
                              AND r.date = :date
                              AND r.time.id = :timeId
                        """, Boolean.class)
                .setParameter("memberId",memberId)
                .setParameter("themeId", themeId)
                .setParameter("date", date)
                .setParameter("timeId", timeId)
                .getSingleResult();
    }
}
