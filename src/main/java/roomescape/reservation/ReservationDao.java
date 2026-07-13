package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

@Repository
public class ReservationDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> findAll() {
        return entityManager.createQuery(
                        "select r from Reservation r join fetch r.member join fetch r.theme join fetch r.time",
                        Reservation.class
                )
                .getResultList();
    }

    public Reservation save(String date, Member member, Long timeId, Long themeId) {
        Time time = entityManager.getReference(Time.class, timeId);
        Theme theme = entityManager.getReference(Theme.class, themeId);
        Reservation reservation = new Reservation(date, member, time, theme);
        entityManager.persist(reservation);
        return reservation;
    }

    public boolean deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation == null) {
            return false;
        }
        entityManager.remove(reservation);
        return true;
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "select r from Reservation r join fetch r.member join fetch r.theme join fetch r.time "
                                + "where r.member.id = :memberId",
                        Reservation.class
                )
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        return findByDateAndThemeId(date, themeId);
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return entityManager.createQuery(
                        "select r from Reservation r join fetch r.member join fetch r.theme join fetch r.time "
                                + "where r.date = :date and r.theme.id = :themeId",
                        Reservation.class
                )
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }
}
