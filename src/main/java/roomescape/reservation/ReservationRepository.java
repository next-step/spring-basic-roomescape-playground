package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.time t " +
            "JOIN FETCH r.theme th " +
            "WHERE r.date = :date AND th.id = :themeId")
    List<Reservation> findByDateAndThemeId(@Param("date") String date, @Param("themeId") Long themeId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.time t " +
            "JOIN FETCH r.theme th " +
            "WHERE r.member.id = :memberId")
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.time t " +
            "JOIN FETCH r.theme th " +
            "JOIN FETCH r.member m")
    List<Reservation> findAllWithRelations();
}
