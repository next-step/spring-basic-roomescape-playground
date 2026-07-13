package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

@Repository
@Transactional
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> findAll() {
        return entityManager.createQuery(
                "SELECT r FROM Reservation r",
                Reservation.class
        ).getResultList();
    }

    public Reservation save(ReservationRequest request, Member member) {

        Theme theme = entityManager.find(Theme.class, request.theme());

        Time time = entityManager.find(Time.class, request.time());

        Reservation reservation = new Reservation(
                member.getName(),
                request.date(),
                time,
                theme,
                member
        );

        entityManager.persist(reservation);

        return reservation;
    }

    public void deleteById(Long id) {

        Reservation reservation =
                entityManager.find(Reservation.class, id);

        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {

        return entityManager.createQuery(
                        """
                                SELECT r
                                FROM Reservation r
                                WHERE r.date = :date
                                AND r.theme.id = :themeId
                                """,
                        Reservation.class
                )
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return entityManager.createQuery(
                        """
                                SELECT r
                                FROM Reservation r
                                WHERE r.member.id = :memberId
                                """,
                        Reservation.class
                )
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
