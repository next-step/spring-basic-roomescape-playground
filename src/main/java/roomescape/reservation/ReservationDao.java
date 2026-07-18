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

    public List<ReservationResponse> findAll() {
        return entityManager.createQuery(
                        "select new roomescape.reservation.ReservationResponse(r.id, m.name, th.name, r.date, t.value) "
                                + "from Reservation r "
                                + "join r.member m "
                                + "join r.theme th "
                                + "join r.time t",
                        ReservationResponse.class
                )
                .getResultList();
    }

    public Reservation save(String date, Member member, Time time, Theme theme) {
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

    public List<ReservationMineResponse> findByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "select new roomescape.reservation.ReservationMineResponse(r.id, th.name, r.date, t.value, '예약') "
                                + "from Reservation r "
                                + "join r.theme th "
                                + "join r.time t "
                                + "where r.member.id = :memberId",
                        ReservationMineResponse.class
                )
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Long> findReservedTimeIdsByDateAndThemeId(String date, Long themeId) {
        return entityManager.createQuery(
                        "select t.id from Reservation r "
                                + "join r.time t "
                                + "where r.date = :date and r.theme.id = :themeId",
                        Long.class
                )
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }
}
