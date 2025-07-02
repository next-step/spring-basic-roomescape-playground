package roomescape.reservation;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;

import java.util.List;

@Repository
public class ReservationRepository {

    private final EntityManager em;

    public ReservationRepository(EntityManager em) {
        this.em = em;
    }

    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM Reservation r";
        return em.createQuery(jpql, Reservation.class).getResultList();
    }

    public List<Reservation> findByMember(Member member) {
        String jpql = "SELECT r FROM Reservation r WHERE r.member = :member";
        return em.createQuery(jpql, Reservation.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<Reservation> findByDateAndTheme(String date, Theme theme) {
        String jpql = "SELECT r FROM Reservation r WHERE r.date = :date AND r.theme = :theme";
        return em.createQuery(jpql, Reservation.class)
                .setParameter("date", date)
                .setParameter("theme", theme)
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
