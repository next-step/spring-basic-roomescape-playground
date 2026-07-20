package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationDao extends JpaRepository<Reservation, Long> {
    @Query("select new roomescape.reservation.ReservationResponse(r.id, m.name, th.name, r.date, t.value) "
            + "from Reservation r join r.member m join r.theme th join r.time t")
    List<ReservationResponse> findAllResponses();

    @Query("select new roomescape.reservation.ReservationMineResponse(r.id, th.name, r.date, t.value, '예약') "
            + "from Reservation r join r.theme th join r.time t where r.member.id = :memberId")
    List<ReservationMineResponse> findByMemberId(@Param("memberId") Long memberId);

    @Query("select t.id from Reservation r join r.time t "
            + "where r.date = :date and r.theme.id = :themeId")
    List<Long> findReservedTimeIdsByDateAndThemeId(@Param("date") String date, @Param("themeId") Long themeId);
}
