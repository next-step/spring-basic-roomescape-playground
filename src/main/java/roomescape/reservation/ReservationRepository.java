package roomescape.reservation;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;

import java.util.List;
import roomescape.time.Time;

@Repository
public class ReservationRepository {

    private final EntityManager em;

    public ReservationRepository(EntityManager em) {
        this.em = em;
    }

    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.member m " +
                "JOIN FETCH r.theme th " +
                "JOIN FETCH r.time t";

        return em.createQuery(jpql, Reservation.class)
                .getResultList();
    }

    public List<Reservation> findByMember(Member member) {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.theme th " +
                "JOIN FETCH r.time t " +
                "WHERE r.member = :member";

        return em.createQuery(jpql, Reservation.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<Reservation> findByDateAndTheme(String date, Theme theme) {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.member m " +
                "JOIN FETCH r.time t " +
                "WHERE r.date = :date AND r.theme = :theme";

        return em.createQuery(jpql, Reservation.class)
                .setParameter("date", date)
                .setParameter("theme", theme)
                .getResultList();
    }

    public List<Reservation> findByDateAndThemeAndTime(String date, Theme theme, Time time) {
        String jpql = "SELECT r FROM Reservation r " +
                "JOIN FETCH r.member m " +
                "JOIN FETCH r.theme th " +
                "WHERE r.date = :date AND th = :theme AND r.time = :time";

        return em.createQuery(jpql, Reservation.class)
                .setParameter("date", date)
                .setParameter("theme", theme)
                .setParameter("time", time)
                .getResultList();
    }

    public Reservation save(Reservation reservation) {
        em.persist(reservation);
        return reservation;
    }

    public void deleteById(Long id) {
        em.remove(em.find(Reservation.class, id));
    }



}
