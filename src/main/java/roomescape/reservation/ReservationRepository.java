package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.member WHERE r.date = :date AND r.time = :time AND r.theme = :theme")
    Optional<Reservation> findWithMemberByDateAndTimeAndTheme(String date, Time time, Theme theme);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time")
    List<Reservation> findAll();

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time WHERE r.member = :member")
    List<Reservation> findByMember(Member member);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
