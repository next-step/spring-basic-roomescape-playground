package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    boolean existsByDateAndTimeAndThemeAndMember(String date, Time time, Theme theme, Member member);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time")
    List<Reservation> findAll();

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time WHERE r.member = :member")
    List<Reservation> findByMember(Member member);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
