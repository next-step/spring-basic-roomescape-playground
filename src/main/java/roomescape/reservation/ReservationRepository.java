package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roomescape.member.Member;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time")
    List<Reservation> findAll();

    List<Reservation> findByMember(Member member);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
