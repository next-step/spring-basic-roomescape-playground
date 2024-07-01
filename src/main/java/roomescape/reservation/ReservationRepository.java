package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//    @Query("select r from Reservation r where r.date = :date and r.theme.id = :themeId")
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

//    @Query("select r from Reservation r where r.member.id = :memberId")
    List<Reservation> findByMemberId(Long memberId);
}
