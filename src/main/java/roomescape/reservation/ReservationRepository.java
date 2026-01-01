package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            em.persist(reservation);
            return reservation;
        }
        return em.merge(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(em.find(Reservation.class, id));
    }

    public List<Reservation> findAll() {
        return em.createQuery("select t from Reservation t", Reservation.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        Reservation find = em.find(Reservation.class, id);
        if (find != null) {
            em.remove(find);
        }
    }


    public List<Reservation> findByMemberId(Long memberId) {
        return em.createQuery("SELECT t FROM Reservation t WHERE t.member.id = :memberId", Reservation.class)
                .setParameter("memberId", memberId).getResultList();
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return em.createQuery("SELECT t FROM Reservation t WHERE t.date = :date and t.theme.id = :themeId", Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }
}
