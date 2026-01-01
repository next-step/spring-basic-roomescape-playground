package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import roomescape.dto.ReservationRequest;
import roomescape.model.Member;
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

    public Reservation save(ReservationRequest request) {
        Time time = entityManager.find(Time.class, request.time());
        Theme theme = entityManager.find(Theme.class, request.theme());

        Reservation reservation;

        if (request.memberId() != null) {
            Member member = entityManager.find(Member.class, request.memberId());
            reservation = new Reservation(member, request.date(), time, theme);
        } else {
            reservation = new Reservation(request.name(), request.date(), time, theme);
        }

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

    public List<Reservation> findByMemberId(Long memberId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.member.id = :memberId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("memberId", memberId);
        return query.getResultList();
    }
}
