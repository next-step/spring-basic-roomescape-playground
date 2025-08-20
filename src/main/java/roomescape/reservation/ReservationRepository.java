package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time WHERE r.member.id = :memberId")
    List<Reservation> findByMemberIdWithThemeAndTime(@Param("memberId") Long memberId);
}
